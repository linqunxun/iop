import { Component, Vue, Inject } from 'vue-property-decorator';

import ModelService from '@/entities/model/model.service';
import { IModel } from '@/shared/model/model.model';

import { IScenes, Scenes } from '@/shared/model/scenes.model';
import ScenesService from './scenes.service';

const validations: any = {
  scenes: {
    name: {},
    cover: {},
    desc: {},
    dataSpec: {},
  },
};

@Component({
  validations,
})
export default class ScenesUpdate extends Vue {
  @Inject('scenesService') private scenesService: () => ScenesService;
  public scenes: IScenes = new Scenes();

  @Inject('modelService') private modelService: () => ModelService;

  public models: IModel[] = [];
  public isSaving = false;
  public currentLanguage = '';

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.scenesId) {
        vm.retrieveScenes(to.params.scenesId);
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
  }

  public save(): void {
    this.isSaving = true;
    if (this.scenes.id) {
      this.scenesService()
        .update(this.scenes)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('iopApp.scenes.updated', { param: param.id });
          return this.$root.$bvToast.toast(message.toString(), {
            toaster: 'b-toaster-top-center',
            title: 'Info',
            variant: 'info',
            solid: true,
            autoHideDelay: 5000,
          });
        });
    } else {
      this.scenesService()
        .create(this.scenes)
        .then(param => {
          this.isSaving = false;
          this.$router.go(-1);
          const message = this.$t('iopApp.scenes.created', { param: param.id });
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

  public retrieveScenes(scenesId): void {
    this.scenesService()
      .find(scenesId)
      .then(res => {
        this.scenes = res;
      });
  }

  public previousState(): void {
    this.$router.go(-1);
  }

  public initRelationships(): void {
    this.modelService()
      .retrieve()
      .then(res => {
        this.models = res.data;
      });
  }
}
