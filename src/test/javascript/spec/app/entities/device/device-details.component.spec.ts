/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';
import VueRouter from 'vue-router';

import * as config from '@/shared/config/config';
import DeviceDetailComponent from '@/entities/device/device-details.vue';
import DeviceClass from '@/entities/device/device-details.component';
import DeviceService from '@/entities/device/device.service';
import router from '@/router';

const localVue = createLocalVue();
localVue.use(VueRouter);

config.initVueApp(localVue);
const i18n = config.initI18N(localVue);
const store = config.initVueXStore(localVue);
localVue.component('font-awesome-icon', {});
localVue.component('router-link', {});

describe('Component Tests', () => {
  describe('Device Management Detail Component', () => {
    let wrapper: Wrapper<DeviceClass>;
    let comp: DeviceClass;
    let deviceServiceStub: SinonStubbedInstance<DeviceService>;

    beforeEach(() => {
      deviceServiceStub = sinon.createStubInstance<DeviceService>(DeviceService);

      wrapper = shallowMount<DeviceClass>(DeviceDetailComponent, {
        store,
        i18n,
        localVue,
        router,
        provide: { deviceService: () => deviceServiceStub },
      });
      comp = wrapper.vm;
    });

    describe('OnInit', () => {
      it('Should call load all on init', async () => {
        // GIVEN
        const foundDevice = { id: 123 };
        deviceServiceStub.find.resolves(foundDevice);

        // WHEN
        comp.retrieveDevice(123);
        await comp.$nextTick();

        // THEN
        expect(comp.device).toBe(foundDevice);
      });
    });

    describe('Before route enter', () => {
      it('Should retrieve data', async () => {
        // GIVEN
        const foundDevice = { id: 123 };
        deviceServiceStub.find.resolves(foundDevice);

        // WHEN
        comp.beforeRouteEnter({ params: { deviceId: 123 } }, null, cb => cb(comp));
        await comp.$nextTick();

        // THEN
        expect(comp.device).toBe(foundDevice);
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
