import { Component, Vue, Inject } from 'vue-property-decorator';

import { IBrand, Brand } from '@/shared/model/brand.model';
import BrandService from './brand.service';

const validations: any = {
  brand: {
    name: {},
    cover: {},
  },
};

@Component({
  validations,
})
export default class BrandUpdate extends Vue {
  @Inject('brandService') private brandService: () => BrandService;
  public brand: IBrand = new Brand();
  public isSaving = false;
  public currentLanguage = '';

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.brandId) {
        vm.retrieveBrand(to.params.brandId);
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
    if (this.brand.id) {
      this.brandService()
        .update(this.brand)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('iopApp.brand.updated', { param: param.id });
          return this.$root.$bvToast.toast(message.toString(), {
            toaster: 'b-toaster-top-center',
            title: 'Info',
            variant: 'info',
            solid: true,
            autoHideDelay: 5000,
          });
        });
    } else {
      this.brandService()
        .create(this.brand)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('iopApp.brand.created', { param: param.id });
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

  public retrieveBrand(brandId): void {
    this.brandService()
      .find(brandId)
      .then(res => {
        this.brand = res;
      });
  }

  public previousState(): void {
    this.$router.go(-1);
  }

  public initRelationships(): void {}
}
