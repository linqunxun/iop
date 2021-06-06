/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import VueRouter from 'vue-router';

import * as config from '@/shared/config/config';
import DockingDetailComponent from '@/entities/docking/docking-details.vue';
import DockingClass from '@/entities/docking/docking-details.component';
import DockingService from '@/entities/docking/docking.service';
import router from '@/router';

const localVue = createLocalVue();
localVue.use(VueRouter);

config.initVueApp(localVue);
const i18n = config.initI18N(localVue);
const store = config.initVueXStore(localVue);
localVue.component('font-awesome-icon', {});
localVue.component('router-link', {});

describe('Component Tests', () => {
  describe('Docking Management Detail Component', () => {
    let wrapper: Wrapper<DockingClass>;
    let comp: DockingClass;
    let dockingServiceStub: SinonStubbedInstance<DockingService>;

    beforeEach(() => {
      dockingServiceStub = sinon.createStubInstance<DockingService>(DockingService);

      wrapper = shallowMount<DockingClass>(DockingDetailComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: { dockingService: () => dockingServiceStub },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundDocking = { id: 123 };
        dockingServiceStub.find.resolves(foundDocking);

        // WHEN
        comp.retrieveDocking(123);
        await comp.$nextTick();

        // THEN
        expect(comp.docking).toBe(foundDocking);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundDocking = { id: 123 };
        dockingServiceStub.find.resolves(foundDocking);

        // WHEN
        comp.beforeRouteEnter({ params: { dockingId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.docking).toBe(foundDocking);
      });
    });

    describe('Previous state', () => {
      it('Should go previous state', async () => {
        comp.previousState();
        await comp.$nextTick();

        expect(comp.$router.currentRoute.fullPath).toContain('/');
      });
    });
  });
});
