<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2 id="iopApp.model.home.createOrEditLabel" data-cy="ModelCreateUpdateHeading" v-text="$t('iopApp.model.home.createOrEditLabel')">
          Create or edit a Model
        </h2>
        <div>
          <div class="form-group" v-if="model.id">
            <label for="id" v-text="$t('global.field.id')">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="model.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('iopApp.model.name')" for="model-name">Name</label>
            <input
              type="text"
              class="form-control"
              name="name"
              id="model-name"
              data-cy="name"
              :class="{ valid: !$v.model.name.$invalid, invalid: $v.model.name.$invalid }"
              v-model="$v.model.name.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('iopApp.model.codeName')" for="model-codeName">Code Name</label>
            <input
              type="text"
              class="form-control"
              name="codeName"
              id="model-codeName"
              data-cy="codeName"
              :class="{ valid: !$v.model.codeName.$invalid, invalid: $v.model.codeName.$invalid }"
              v-model="$v.model.codeName.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('iopApp.model.brand')" for="model-brand">Brand</label>
            <select class="form-control" id="model-brand" data-cy="brand" name="brand" v-model="model.brand">
              <option v-bind:value="null"></option>
              <option
                v-bind:value="model.brand && brandOption.id === model.brand.id ? model.brand : brandOption"
                v-for="brandOption in brands"
                :key="brandOption.id"
              >
                {{ brandOption.name }}
              </option>
            </select>
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('iopApp.model.docking')" for="model-docking">Docking</label>
            <select class="form-control" id="model-docking" data-cy="docking" name="docking" v-model="model.docking">
              <option v-bind:value="null"></option>
              <option
                v-bind:value="model.docking && dockingOption.id === model.docking.id ? model.docking : dockingOption"
                v-for="dockingOption in dockings"
                :key="dockingOption.id"
              >
                {{ dockingOption.id }}
              </option>
            </select>
          </div>
          <div class="form-group">
            <label v-text="$t('iopApp.model.scenes')" for="model-scenes">Scenes</label>
            <select
              class="form-control"
              id="model-scenes"
              data-cy="scenes"
              multiple
              name="scenes"
              v-if="model.scenes !== undefined"
              v-model="model.scenes"
            >
              <option v-bind:value="getSelected(model.scenes, scenesOption)" v-for="scenesOption in scenes" :key="scenesOption.id">
                {{ scenesOption.name }}
              </option>
            </select>
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
            :disabled="$v.model.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.save')">Save</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./model-update.component.ts"></script>
