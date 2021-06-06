/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import VueRouter from 'vue-router';

import * as config from '@/shared/config/config';
import ScenesDetailComponent from '@/entities/scenes/scenes-details.vue';
import ScenesClass from '@/entities/scenes/scenes-details.component';
import ScenesService from '@/entities/scenes/scenes.service';
import router from '@/router';

const localVue = createLocalVue();
localVue.use(VueRouter);

config.initVueApp(localVue);
const i18n = config.initI18N(localVue);
const store = config.initVueXStore(localVue);
localVue.component('font-awesome-icon', {});
localVue.component('router-link', {});

describe('Component Tests', () => {
  describe('Scenes Management Detail Component', () => {
    let wrapper: Wrapper<ScenesClass>;
    let comp: ScenesClass;
    let scenesServiceStub: SinonStubbedInstance<ScenesService>;

    beforeEach(() => {
      scenesServiceStub = sinon.createStubInstance<ScenesService>(ScenesService);

      wrapper = shallowMount<ScenesClass>(ScenesDetailComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: { scenesService: () => scenesServiceStub },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundScenes = { id: 123 };
        scenesServiceStub.find.resolves(foundScenes);

        // WHEN
        comp.retrieveScenes(123);
        await comp.$nextTick();

        // THEN
        expect(comp.scenes).toBe(foundScenes);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundScenes = { id: 123 };
        scenesServiceStub.find.resolves(foundScenes);

        // WHEN
        comp.beforeRouteEnter({ params: { scenesId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.scenes).toBe(foundScenes);
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
