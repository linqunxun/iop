/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import Router from 'vue-router';

import * as config from '@/shared/config/config';
import ScenesUpdateComponent from '@/entities/scenes/scenes-update.vue';
import ScenesClass from '@/entities/scenes/scenes-update.component';
import ScenesService from '@/entities/scenes/scenes.service';

import ModelService from '@/entities/model/model.service';

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
  describe('Scenes Management Update Component', () => {
    let wrapper: Wrapper<ScenesClass>;
    let comp: ScenesClass;
    let scenesServiceStub: SinonStubbedInstance<ScenesService>;

    beforeEach(() => {
      scenesServiceStub = sinon.createStubInstance<ScenesService>(ScenesService);

      wrapper = shallowMount<ScenesClass>(ScenesUpdateComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: {
          scenesService: () => scenesServiceStub,

          modelService: () => new ModelService(),
        },
      });
      comp = wrapper.vm;
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const entity = { id: 123 };
        comp.scenes = entity;
        scenesServiceStub.update.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(scenesServiceStub.update.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        comp.scenes = entity;
        scenesServiceStub.create.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(scenesServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundScenes = { id: 123 };
        scenesServiceStub.find.resolves(foundScenes);
        scenesServiceStub.retrieve.resolves([foundScenes]);

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
