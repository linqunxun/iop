/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import VueRouter from 'vue-router';

import * as config from '@/shared/config/config';
import EnterpriseDetailComponent from '@/entities/enterprise/enterprise-details.vue';
import EnterpriseClass from '@/entities/enterprise/enterprise-details.component';
import EnterpriseService from '@/entities/enterprise/enterprise.service';
import router from '@/router';

const localVue = createLocalVue();
localVue.use(VueRouter);

config.initVueApp(localVue);
const i18n = config.initI18N(localVue);
const store = config.initVueXStore(localVue);
localVue.component('font-awesome-icon', {});
localVue.component('router-link', {});

describe('Component Tests', () => {
  describe('Enterprise Management Detail Component', () => {
    let wrapper: Wrapper<EnterpriseClass>;
    let comp: EnterpriseClass;
    let enterpriseServiceStub: SinonStubbedInstance<EnterpriseService>;

    beforeEach(() => {
      enterpriseServiceStub = sinon.createStubInstance<EnterpriseService>(EnterpriseService);

      wrapper = shallowMount<EnterpriseClass>(EnterpriseDetailComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: { enterpriseService: () => enterpriseServiceStub },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundEnterprise = { id: 123 };
        enterpriseServiceStub.find.resolves(foundEnterprise);

        // WHEN
        comp.retrieveEnterprise(123);
        await comp.$nextTick();

        // THEN
        expect(comp.enterprise).toBe(foundEnterprise);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundEnterprise = { id: 123 };
        enterpriseServiceStub.find.resolves(foundEnterprise);

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
