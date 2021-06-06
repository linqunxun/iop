<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="docking">
        <h2 class="jh-entity-heading" data-cy="dockingDetailsHeading">
          <span v-text="$t('iopApp.docking.detail.title')">Docking</span> {{ docking.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="$t('iopApp.docking.name')">Name</span>
          </dt>
          <dd>
            <span>{{ docking.name }}</span>
          </dd>
          <dt>
            <span v-text="$t('iopApp.docking.variable')">Variable</span>
          </dt>
          <dd>
            <span>{{ docking.variable }}</span>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.back')"> Back</span>
        </button>
        <router-link v-if="docking.id" :to="{ name: 'DockingEdit', params: { dockingId: docking.id } }" custom v-slot="{ navigate }">
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.edit')"> Edit</span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./docking-details.component.ts"></script>
