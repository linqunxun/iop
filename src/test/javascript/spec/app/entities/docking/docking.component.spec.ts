/* tslint:disable max-line-length */
import { shallowMount, createLocalVue, Wrapper } from '@vue/test-utils';
import sinon, { SinonStubbedInstance } from 'sinon';

import * as config from '@/shared/config/config';
import DockingComponent from '@/entities/docking/docking.vue';
import DockingClass from '@/entities/docking/docking.component';
import DockingService from '@/entities/docking/docking.service';

const localVue = createLocalVue();

config.initVueApp(localVue);
const i18n = config.initI18N(localVue);
const store = config.initVueXStore(localVue);
localVue.component('font-awesome-icon', {});
localVue.component('b-badge', {});
localVue.directive('b-modal', {});
localVue.component('b-button', {});
localVue.component('router-link', {});

const bModalStub = {
  render: () => {},
  methods: {
    hide: () => {},
    show: () => {},
  },
};

describe('Component Tests', () => {
  describe('Docking Management Component', () => {
    let wrapper: Wrapper<DockingClass>;
    let comp: DockingClass;
    let dockingServiceStub: SinonStubbedInstance<DockingService>;

    beforeEach(() => {
      dockingServiceStub = sinon.createStubInstance<DockingService>(DockingService);
      dockingServiceStub.retrieve.resolves({ headers: {} });

      wrapper = shallowMount<DockingClass>(DockingComponent, {
        store,
        i18n,
        localVue,
        stubs: { bModal: bModalStub as any },
        provide: {
          dockingService: () => dockingServiceStub,
        },
      });
      comp = wrapper.vm;
    });

    it('Should call load all on init', async () => {
      // GIVEN
      dockingServiceStub.retrieve.resolves({ headers: {}, data: [{ id: 123 }] });

      // WHEN
      comp.retrieveAllDockings();
      await comp.$nextTick();

      // THEN
      expect(dockingServiceStub.retrieve.called).toBeTruthy();
      expect(comp.dockings[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
    it('Should call delete service on confirmDelete', async () => {
      // GIVEN
      dockingServiceStub.delete.resolves({});

      // WHEN
      comp.prepareRemove({ id: 123 });
      comp.removeDocking();
      await comp.$nextTick();

      // THEN
      expect(dockingServiceStub.delete.called).toBeTruthy();
      expect(dockingServiceStub.retrieve.callCount).toEqual(1);
    });
  });
});
