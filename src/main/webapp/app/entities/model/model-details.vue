<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="model">
        <h2 class="jh-entity-heading" data-cy="modelDetailsHeading">
          <span v-text="$t('iopApp.model.detail.title')">Model</span> {{ model.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="$t('iopApp.model.name')">Name</span>
          </dt>
          <dd>
            <span>{{ model.name }}</span>
          </dd>
          <dt>
            <span v-text="$t('iopApp.model.codeName')">Code Name</span>
          </dt>
          <dd>
            <span>{{ model.codeName }}</span>
          </dd>
          <dt>
            <span v-text="$t('iopApp.model.brand')">Brand</span>
          </dt>
          <dd>
            <div v-if="model.brand">
              <router-link :to="{ name: 'BrandView', params: { brandId: model.brand.id } }">{{ model.brand.name }}</router-link>
            </div>
          </dd>
          <dt>
            <span v-text="$t('iopApp.model.docking')">Docking</span>
          </dt>
          <dd>
            <div v-if="model.docking">
              <router-link :to="{ name: 'DockingView', params: { dockingId: model.docking.id } }">{{ model.docking.id }}</router-link>
            </div>
          </dd>
          <dt>
            <span v-text="$t('iopApp.model.scenes')">Scenes</span>
          </dt>
          <dd>
            <span v-for="(scenes, i) in model.scenes" :key="scenes.id"
              >{{ i > 0 ? ', ' : '' }}
              <router-link :to="{ name: 'ScenesView', params: { scenesId: scenes.id } }">{{ scenes.name }}</router-link>
            </span>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.back')"> Back</span>
        </button>
        <router-link v-if="model.id" :to="{ name: 'ModelEdit', params: { modelId: model.id } }" custom v-slot="{ navigate }">
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.edit')"> Edit</span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./model-details.component.ts"></script>
