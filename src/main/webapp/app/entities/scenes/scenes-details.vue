<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="scenes">
        <h2 class="jh-entity-heading" data-cy="scenesDetailsHeading">
          <span v-text="$t('iopApp.scenes.detail.title')">Scenes</span> {{ scenes.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="$t('iopApp.scenes.name')">Name</span>
          </dt>
          <dd>
            <span>{{ scenes.name }}</span>
          </dd>
          <dt>
            <span v-text="$t('iopApp.scenes.cover')">Cover</span>
          </dt>
          <dd>
            <span>{{ scenes.cover }}</span>
          </dd>
          <dt>
            <span v-text="$t('iopApp.scenes.desc')">Desc</span>
          </dt>
          <dd>
            <span>{{ scenes.desc }}</span>
          </dd>
          <dt>
            <span v-text="$t('iopApp.scenes.dataSpec')">Data Spec</span>
          </dt>
          <dd>
            <span>{{ scenes.dataSpec }}</span>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.back')"> Back</span>
        </button>
        <router-link v-if="scenes.id" :to="{ name: 'ScenesEdit', params: { scenesId: scenes.id } }" custom v-slot="{ navigate }">
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.edit')"> Edit</span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./scenes-details.component.ts"></script>
