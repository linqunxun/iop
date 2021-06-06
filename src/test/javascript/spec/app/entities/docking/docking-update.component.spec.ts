/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import Router from 'vue-router';

import * as config from '@/shared/config/config';
import DockingUpdateComponent from '@/entities/docking/docking-update.vue';
import DockingClass from '@/entities/docking/docking-update.component';
import DockingService from '@/entities/docking/docking.service';

const localVue = createLocalVue();

config.initVueApp(localVue);
const i18n = config.initI18N(localVue);
const store = config.initVueXStore(localVue);
const router = new Router();
localVue.use(Router);
localVue.component('font-awesome-icon', {});
localVue.component('b-input-group', {});
localVue.component('b-input-group-prepend', {});
localVue.component('b-form-datepicker', {});
localVue.component('b-form-input', {});

describe('Component Tests', () => {
  describe('Docking Management Update Component', () => {
    let wrapper: Wrapper<DockingClass>;
    let comp: DockingClass;
    let dockingServiceStub: SinonStubbedInstance<DockingService>;

    beforeEach(() => {
      dockingServiceStub = sinon.createStubInstance<DockingService>(DockingService);

      wrapper = shallowMount<DockingClass>(DockingUpdateComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: {
          dockingService: () => dockingServiceStub,
        },
      });
      comp = wrapper.vm;
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const entity = { id: 123 };
        comp.docking = entity;
        dockingServiceStub.update.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(dockingServiceStub.update.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        comp.docking = entity;
        dockingServiceStub.create.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(dockingServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundDocking = { id: 123 };
        dockingServiceStub.find.resolves(foundDocking);
        dockingServiceStub.retrieve.resolves([foundDocking]);

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
