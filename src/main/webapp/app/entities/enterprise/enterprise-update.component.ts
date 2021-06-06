import { Component, Vue, Inject } from 'vue-property-decorator';

import { IEnterprise, Enterprise } from '@/shared/model/enterprise.model';
import EnterpriseService from './enterprise.service';

const validations: any = {
  enterprise: {
    name: {},
    areaCode: {},
  },
};

@Component({
  validations,
})
export default class EnterpriseUpdate extends Vue {
  @Inject('enterpriseService') private enterpriseService: () => EnterpriseService;
  public enterprise: IEnterprise = new Enterprise();
  public isSaving = false;
  public currentLanguage = '';

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.enterpriseId) {
        vm.retrieveEnterprise(to.params.enterpriseId);
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
    if (this.enterprise.id) {
      this.enterpriseService()
        .update(this.enterprise)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('iopApp.enterprise.updated', { param: param.id });
          return this.$root.$bvToast.toast(message.toString(), {
            toaster: 'b-toaster-top-center',
            title: 'Info',
            variant: 'info',
            solid: true,
            autoHideDelay: 5000,
          });
        });
    } else {
      this.enterpriseService()
        .create(this.enterprise)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('iopApp.enterprise.created', { param: param.id });
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

  public retrieveEnterprise(enterpriseId): void {
    this.enterpriseService()
      .find(enterpriseId)
      .then(res => {
        this.enterprise = res;
      });
  }

  public previousState(): void {
    this.$router.go(-1);
  }

  public initRelationships(): void {}
}
