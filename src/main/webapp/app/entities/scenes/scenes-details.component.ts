import { Component, Vue, Inject } from 'vue-property-decorator';

import { IScenes } from '@/shared/model/scenes.model';
import ScenesService from './scenes.service';

@Component
export default class ScenesDetails extends Vue {
  @Inject('scenesService') private scenesService: () => ScenesService;
  public scenes: IScenes = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.scenesId) {
        vm.retrieveScenes(to.params.scenesId);
      }
    });
  }

  public retrieveScenes(scenesId) {
    this.scenesService()
      .find(scenesId)
      .then(res => {
        this.scenes = res;
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}
