<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2
          id="iopApp.docking.home.createOrEditLabel"
          data-cy="DockingCreateUpdateHeading"
          v-text="$t('iopApp.docking.home.createOrEditLabel')"
        >
          Create or edit a Docking
        </h2>
        <div>
          <div class="form-group" v-if="docking.id">
            <label for="id" v-text="$t('global.field.id')">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="docking.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('iopApp.docking.name')" for="docking-name">Name</label>
            <input
              type="text"
              class="form-control"
              name="name"
              id="docking-name"
              data-cy="name"
              :class="{ valid: !$v.docking.name.$invalid, invalid: $v.docking.name.$invalid }"
              v-model="$v.docking.name.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('iopApp.docking.variable')" for="docking-variable">Variable</label>
            <input
              type="text"
              class="form-control"
              name="variable"
              id="docking-variable"
              data-cy="variable"
              :class="{ valid: !$v.docking.variable.$invalid, invalid: $v.docking.variable.$invalid }"
              v-model="$v.docking.variable.$model"
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
            :disabled="$v.docking.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.save')">Save</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./docking-update.component.ts"></script>
