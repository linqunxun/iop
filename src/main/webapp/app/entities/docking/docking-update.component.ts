import { Component, Vue, Inject } from 'vue-property-decorator';

import { IDocking, Docking } from '@/shared/model/docking.model';
import DockingService from './docking.service';

const validations: any = {
  docking: {
    name: {},
    variable: {},
  },
};

@Component({
  validations,
})
export default class DockingUpdate extends Vue {
  @Inject('dockingService') private dockingService: () => DockingService;
  public docking: IDocking = new Docking();
  public isSaving = false;
  public currentLanguage = '';

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.dockingId) {
        vm.retrieveDocking(to.params.dockingId);
      }
    });
  }

  created(): void {
    this.currentLanguage = this.$store.getters.currentLanguage;
    this.$store.watch(
      () => this.$store.getters.currentLanguage,
      () => {
        this.currentLanguage = this.$store.getters.currentLanguage;
      }
    );
  }

  public save(): void {
    this.isSaving = true;
    if (this.docking.id) {
      this.dockingService()
        .update(this.docking)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('iopApp.docking.updated', { param: param.id });
          return this.$root.$bvToast.toast(message.toString(), {
            toaster: 'b-toaster-top-center',
            title: 'Info',
            variant: 'info',
            solid: true,
            autoHideDelay: 5000,
          });
        });
    } else {
      this.dockingService()
        .create(this.docking)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('iopApp.docking.created', { param: param.id });
          this.$root.$bvToast.toast(message.toString(), {
            toaster: 'b-toaster-top-center',
            title: 'Success',
            variant: 'success',
            solid: true,
            autoHideDelay: 5000,
          });
        });
    }
  }

  public retrieveDocking(dockingId): void {
    this.dockingService()
      .find(dockingId)
      .then(res => {
        this.docking = res;
      });
  }

  public previousState(): void {
    this.$router.go(-1);
  }

  public initRelationships(): void {}
}
