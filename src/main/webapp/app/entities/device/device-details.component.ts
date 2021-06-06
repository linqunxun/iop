import { Component, Vue, Inject } from 'vue-property-decorator';

import { IDevice } from '@/shared/model/device.model';
import DeviceService from './device.service';

@Component
export default class DeviceDetails extends Vue {
  @Inject('deviceService') private deviceService: () => DeviceService;
  public device: IDevice = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.deviceId) {
        vm.retrieveDevice(to.params.deviceId);
      }
    });
  }

  public retrieveDevice(deviceId) {
    this.deviceService()
      .find(deviceId)
      .then(res => {
        this.device = res;
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}
