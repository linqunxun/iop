/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import Router from 'vue-router';

import * as config from '@/shared/config/config';
import EnterpriseUpdateComponent from '@/entities/enterprise/enterprise-update.vue';
import EnterpriseClass from '@/entities/enterprise/enterprise-update.component';
import EnterpriseService from '@/entities/enterprise/enterprise.service';

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
  describe('Enterprise Management Update Component', () => {
    let wrapper: Wrapper<EnterpriseClass>;
    let comp: EnterpriseClass;
    let enterpriseServiceStub: SinonStubbedInstance<EnterpriseService>;

    beforeEach(() => {
      enterpriseServiceStub = sinon.createStubInstance<EnterpriseService>(EnterpriseService);

      wrapper = shallowMount<EnterpriseClass>(EnterpriseUpdateComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: {
          enterpriseService: () => enterpriseServiceStub,
        },
      });
      comp = wrapper.vm;
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', async () => {
        // GIVEN
        const entity = { id: 123 };
        comp.enterprise = entity;
        enterpriseServiceStub.update.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(enterpriseServiceStub.update.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', async () => {
        // GIVEN
        const entity = {};
        comp.enterprise = entity;
        enterpriseServiceStub.create.resolves(entity);

        // WHEN
        comp.save();
        await comp.$nextTick();

        // THEN
        expect(enterpriseServiceStub.create.calledWith(entity)).toBeTruthy();
        expect(comp.isSaving).toEqual(false);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundEnterprise = { id: 123 };
        enterpriseServiceStub.find.resolves(foundEnterprise);
        enterpriseServiceStub.retrieve.resolves([foundEnterprise]);

        // WHEN
        comp.beforeRouteEnter({ params: { enterpriseId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.enterprise).toBe(foundEnterprise);
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
