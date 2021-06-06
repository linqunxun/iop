<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2
          id="iopApp.enterprise.home.createOrEditLabel"
          data-cy="EnterpriseCreateUpdateHeading"
          v-text="$t('iopApp.enterprise.home.createOrEditLabel')"
        >
          Create or edit a Enterprise
        </h2>
        <div>
          <div class="form-group" v-if="enterprise.id">
            <label for="id" v-text="$t('global.field.id')">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="enterprise.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('iopApp.enterprise.name')" for="enterprise-name">Name</label>
            <input
              type="text"
              class="form-control"
              name="name"
              id="enterprise-name"
              data-cy="name"
              :class="{ valid: !$v.enterprise.name.$invalid, invalid: $v.enterprise.name.$invalid }"
              v-model="$v.enterprise.name.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('iopApp.enterprise.areaCode')" for="enterprise-areaCode">Area Code</label>
            <input
              type="text"
              class="form-control"
              name="areaCode"
              id="enterprise-areaCode"
              data-cy="areaCode"
              :class="{ valid: !$v.enterprise.areaCode.$invalid, invalid: $v.enterprise.areaCode.$invalid }"
              v-model="$v.enterprise.areaCode.$model"
            />
          </div>
        </div>
        <div>
          <button type="button" id="cancel-save" class="btn btn-secondary" v-on:click="previousState()">
            <font-awesome-icon icon="ban"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.cancel')">Cancel</span>
          </button>
          <button
            type="submit"
            id="save-entity"
            data-cy="entityCreateSaveButton"
            :disabled="$v.enterprise.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.save')">Save</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./enterprise-update.component.ts"></script>
