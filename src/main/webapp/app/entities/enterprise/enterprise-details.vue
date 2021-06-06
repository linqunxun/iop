<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <div v-if="enterprise">
        <h2 class="jh-entity-heading" data-cy="enterpriseDetailsHeading">
          <span v-text="$t('iopApp.enterprise.detail.title')">Enterprise</span> {{ enterprise.id }}
        </h2>
        <dl class="row jh-entity-details">
          <dt>
            <span v-text="$t('iopApp.enterprise.name')">Name</span>
          </dt>
          <dd>
            <span>{{ enterprise.name }}</span>
          </dd>
          <dt>
            <span v-text="$t('iopApp.enterprise.areaCode')">Area Code</span>
          </dt>
          <dd>
            <span>{{ enterprise.areaCode }}</span>
          </dd>
        </dl>
        <button type="submit" v-on:click.prevent="previousState()" class="btn btn-info" data-cy="entityDetailsBackButton">
          <font-awesome-icon icon="arrow-left"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.back')"> Back</span>
        </button>
        <router-link
          v-if="enterprise.id"
          :to="{ name: 'EnterpriseEdit', params: { enterpriseId: enterprise.id } }"
          custom
          v-slot="{ navigate }"
        >
          <button @click="navigate" class="btn btn-primary">
            <font-awesome-icon icon="pencil-alt"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.edit')"> Edit</span>
          </button>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./enterprise-details.component.ts"></script>
