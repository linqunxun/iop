import { Authority } from '@/shared/security/authority';
/* tslint:disable */
// prettier-ignore

// prettier-ignore
const Scenes = () => import('@/entities/scenes/scenes.vue');
// prettier-ignore
const ScenesUpdate = () => import('@/entities/scenes/scenes-update.vue');
// prettier-ignore
const ScenesDetails = () => import('@/entities/scenes/scenes-details.vue');
// prettier-ignore
const Brand = () => import('@/entities/brand/brand.vue');
// prettier-ignore
const BrandUpdate = () => import('@/entities/brand/brand-update.vue');
// prettier-ignore
const BrandDetails = () => import('@/entities/brand/brand-details.vue');
// prettier-ignore
const Model = () => import('@/entities/model/model.vue');
// prettier-ignore
const ModelUpdate = () => import('@/entities/model/model-update.vue');
// prettier-ignore
const ModelDetails = () => import('@/entities/model/model-details.vue');
// prettier-ignore
const Device = () => import('@/entities/device/device.vue');
// prettier-ignore
const DeviceUpdate = () => import('@/entities/device/device-update.vue');
// prettier-ignore
const DeviceDetails = () => import('@/entities/device/device-details.vue');
// prettier-ignore
const Docking = () => import('@/entities/docking/docking.vue');
// prettier-ignore
const DockingUpdate = () => import('@/entities/docking/docking-update.vue');
// prettier-ignore
const DockingDetails = () => import('@/entities/docking/docking-details.vue');
// jhipster-needle-add-entity-to-router-import - JHipster will import entities to the router here

export default [
  {
    path: '/scenes',
    name: 'Scenes',
    component: Scenes,
    meta: { authorities: [Authority.USER] },
  },
  {
    path: '/scenes/new',
    name: 'ScenesCreate',
    component: ScenesUpdate,
    meta: { authorities: [Authority.USER] },
  },
  {
    path: '/scenes/:scenesId/edit',
    name: 'ScenesEdit',
    component: ScenesUpdate,
    meta: { authorities: [Authority.USER] },
  },
  {
    path: '/scenes/:scenesId/view',
    name: 'ScenesView',
    component: ScenesDetails,
    meta: { authorities: [Authority.USER] },
  },
  {
    path: '/brand',
    name: 'Brand',
    component: Brand,
    meta: { authorities: [Authority.USER] },
  },
  {
    path: '/brand/new',
    name: 'BrandCreate',
    component: BrandUpdate,
    meta: { authorities: [Authority.USER] },
  },
  {
    path: '/brand/:brandId/edit',
    name: 'BrandEdit',
    component: BrandUpdate,
    meta: { authorities: [Authority.USER] },
  },
  {
    path: '/brand/:brandId/view',
    name: 'BrandView',
    component: BrandDetails,
    meta: { authorities: [Authority.USER] },
  },
  {
    path: '/model',
    name: 'Model',
    component: Model,
    meta: { authorities: [Authority.USER] },
  },
  {
    path: '/model/new',
    name: 'ModelCreate',
    component: ModelUpdate,
    meta: { authorities: [Authority.USER] },
  },
  {
    path: '/model/:modelId/edit',
    name: 'ModelEdit',
    component: ModelUpdate,
    meta: { authorities: [Authority.USER] },
  },
  {
    path: '/model/:modelId/view',
    name: 'ModelView',
    component: ModelDetails,
    meta: { authorities: [Authority.USER] },
  },
  {
    path: '/device',
    name: 'Device',
    component: Device,
    meta: { authorities: [Authority.USER] },
  },
  {
    path: '/device/new',
    name: 'DeviceCreate',
    component: DeviceUpdate,
    meta: { authorities: [Authority.USER] },
  },
  {
    path: '/device/:deviceId/edit',
    name: 'DeviceEdit',
    component: DeviceUpdate,
    meta: { authorities: [Authority.USER] },
  },
  {
    path: '/device/:deviceId/view',
    name: 'DeviceView',
    component: DeviceDetails,
    meta: { authorities: [Authority.USER] },
  },
  {
    path: '/docking',
    name: 'Docking',
    component: Docking,
    meta: { authorities: [Authority.USER] },
  },
  {
    path: '/docking/new',
    name: 'DockingCreate',
    component: DockingUpdate,
    meta: { authorities: [Authority.USER] },
  },
  {
    path: '/docking/:dockingId/edit',
    name: 'DockingEdit',
    component: DockingUpdate,
    meta: { authorities: [Authority.USER] },
  },
  {
    path: '/docking/:dockingId/view',
    name: 'DockingView',
    component: DockingDetails,
    meta: { authorities: [Authority.USER] },
  },
  // jhipster-needle-add-entity-to-router - JHipster will add entities to the router here
];
