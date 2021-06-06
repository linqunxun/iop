import { Component, Vue, Inject } from 'vue-property-decorator';

import BrandService from '@/entities/brand/brand.service';
import { IBrand } from '@/shared/model/brand.model';

import DockingService from '@/entities/docking/docking.service';
import { IDocking } from '@/shared/model/docking.model';

import ScenesService from '@/entities/scenes/scenes.service';
import { IScenes } from '@/shared/model/scenes.model';

import { IModel, Model } from '@/shared/model/model.model';
import ModelService from './model.service';

const validations: any = {
  model: {
    name: {},
    codeName: {},
  },
};

@Component({
  validations,
})
export default class ModelUpdate extends Vue {
  @Inject('modelService') private modelService: () => ModelService;
  public model: IModel = new Model();

  @Inject('brandService') private brandService: () => BrandService;

  public brands: IBrand[] = [];

  @Inject('dockingService') private dockingService: () => DockingService;

  public dockings: IDocking[] = [];

  @Inject('scenesService') private scenesService: () => ScenesService;

  public scenes: IScenes[] = [];
  public isSaving = false;
  public currentLanguage = '';

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.modelId) {
        vm.retrieveModel(to.params.modelId);
      }
      vm.initRelationships();
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
    this.model.scenes = [];
  }

  public save(): void {
    this.isSaving = true;
    if (this.model.id) {
      this.modelService()
        .update(this.model)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('iopApp.model.updated', { param: param.id });
          return this.$root.$bvToast.toast(message.toString(), {
            toaster: 'b-toaster-top-center',
            title: 'Info',
            variant: 'info',
            solid: true,
            autoHideDelay: 5000,
          });
        });
    } else {
      this.modelService()
        .create(this.model)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('iopApp.model.created', { param: param.id });
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

  public retrieveModel(modelId): void {
    this.modelService()
      .find(modelId)
      .then(res => {
        this.model = res;
      });
  }

  public previousState(): void {
    this.$router.go(-1);
  }

  public initRelationships(): void {
    this.brandService()
      .retrieve()
      .then(res => {
        this.brands = res.data;
      });
    this.dockingService()
      .retrieve()
      .then(res => {
        this.dockings = res.data;
      });
    this.scenesService()
      .retrieve()
      .then(res => {
        this.scenes = res.data;
      });
  }

  public getSelected(selectedVals, option): any {
    if (selectedVals) {
      for (let i = 0; i < selectedVals.length; i++) {
        if (option.id === selectedVals[i].id) {
          return selectedVals[i];
        }
      }
    }
    return option;
  }
}
