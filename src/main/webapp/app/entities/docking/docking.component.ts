import { mixins } from 'vue-class-component';

import { Component, Vue, Inject } from 'vue-property-decorator';
import Vue2Filters from 'vue2-filters';
import { IDocking } from '@/shared/model/docking.model';

import DockingService from './docking.service';

@Component({
  mixins: [Vue2Filters.mixin],
})
export default class Docking extends Vue {
  @Inject('dockingService') private dockingService: () => DockingService;
  private removeId: number = null;

  public dockings: IDocking[] = [];

  public isFetching = false;

  public mounted(): void {
    this.retrieveAllDockings();
  }

  public clear(): void {
    this.retrieveAllDockings();
  }

  public retrieveAllDockings(): void {
    this.isFetching = true;

    this.dockingService()
      .retrieve()
      .then(
        res => {
          this.dockings = res.data;
          this.isFetching = false;
        },
        err => {
          this.isFetching = false;
        }
      );
  }

  public handleSyncList(): void {
    this.clear();
  }

  public prepareRemove(instance: IDocking): void {
    this.removeId = instance.id;
    if (<any>this.$refs.removeEntity) {
      (<any>this.$refs.removeEntity).show();
    }
  }

  public removeDocking(): void {
    this.dockingService()
      .delete(this.removeId)
      .then(() => {
        const message = this.$t('iopApp.docking.deleted', { param: this.removeId });
        this.$bvToast.toast(message.toString(), {
          toaster: 'b-toaster-top-center',
          title: 'Info',
          variant: 'danger',
          solid: true,
          autoHideDelay: 5000,
        });
        this.removeId = null;
        this.retrieveAllDockings();
        this.closeDialog();
      });
  }

  public closeDialog(): void {
    (<any>this.$refs.removeEntity).hide();
  }
}
