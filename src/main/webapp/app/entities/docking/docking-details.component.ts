import { Component, Vue, Inject } from 'vue-property-decorator';

import { IDocking } from '@/shared/model/docking.model';
import DockingService from './docking.service';

@Component
export default class DockingDetails extends Vue {
  @Inject('dockingService') private dockingService: () => DockingService;
  public docking: IDocking = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.dockingId) {
        vm.retrieveDocking(to.params.dockingId);
      }
    });
  }

  public retrieveDocking(dockingId) {
    this.dockingService()
      .find(dockingId)
      .then(res => {
        this.docking = res;
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}
