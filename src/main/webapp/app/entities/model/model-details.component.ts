import { Component, Vue, Inject } from 'vue-property-decorator';

import { IModel } from '@/shared/model/model.model';
import ModelService from './model.service';

@Component
export default class ModelDetails extends Vue {
  @Inject('modelService') private modelService: () => ModelService;
  public model: IModel = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.modelId) {
        vm.retrieveModel(to.params.modelId);
      }
    });
  }

  public retrieveModel(modelId) {
    this.modelService()
      .find(modelId)
      .then(res => {
        this.model = res;
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}
