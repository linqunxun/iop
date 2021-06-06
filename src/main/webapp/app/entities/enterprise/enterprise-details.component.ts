import { Component, Vue, Inject } from 'vue-property-decorator';

import { IEnterprise } from '@/shared/model/enterprise.model';
import EnterpriseService from './enterprise.service';

@Component
export default class EnterpriseDetails extends Vue {
  @Inject('enterpriseService') private enterpriseService: () => EnterpriseService;
  public enterprise: IEnterprise = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.enterpriseId) {
        vm.retrieveEnterprise(to.params.enterpriseId);
      }
    });
  }

  public retrieveEnterprise(enterpriseId) {
    this.enterpriseService()
      .find(enterpriseId)
      .then(res => {
        this.enterprise = res;
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}
