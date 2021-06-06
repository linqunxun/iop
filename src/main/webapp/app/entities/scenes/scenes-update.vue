<template>
  <div class="row justify-content-center">
    <div class="col-8">
      <form name="editForm" role="form" novalidate v-on:submit.prevent="save()">
        <h2
          id="iopApp.scenes.home.createOrEditLabel"
          data-cy="ScenesCreateUpdateHeading"
          v-text="$t('iopApp.scenes.home.createOrEditLabel')"
        >
          Create or edit a Scenes
        </h2>
        <div>
          <div class="form-group" v-if="scenes.id">
            <label for="id" v-text="$t('global.field.id')">ID</label>
            <input type="text" class="form-control" id="id" name="id" v-model="scenes.id" readonly />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('iopApp.scenes.name')" for="scenes-name">Name</label>
            <input
              type="text"
              class="form-control"
              name="name"
              id="scenes-name"
              data-cy="name"
              :class="{ valid: !$v.scenes.name.$invalid, invalid: $v.scenes.name.$invalid }"
              v-model="$v.scenes.name.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('iopApp.scenes.cover')" for="scenes-cover">Cover</label>
            <input
              type="text"
              class="form-control"
              name="cover"
              id="scenes-cover"
              data-cy="cover"
              :class="{ valid: !$v.scenes.cover.$invalid, invalid: $v.scenes.cover.$invalid }"
              v-model="$v.scenes.cover.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('iopApp.scenes.desc')" for="scenes-desc">Desc</label>
            <input
              type="text"
              class="form-control"
              name="desc"
              id="scenes-desc"
              data-cy="desc"
              :class="{ valid: !$v.scenes.desc.$invalid, invalid: $v.scenes.desc.$invalid }"
              v-model="$v.scenes.desc.$model"
            />
          </div>
          <div class="form-group">
            <label class="form-control-label" v-text="$t('iopApp.scenes.dataSpec')" for="scenes-dataSpec">Data Spec</label>
            <input
              type="text"
              class="form-control"
              name="dataSpec"
              id="scenes-dataSpec"
              data-cy="dataSpec"
              :class="{ valid: !$v.scenes.dataSpec.$invalid, invalid: $v.scenes.dataSpec.$invalid }"
              v-model="$v.scenes.dataSpec.$model"
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
            :disabled="$v.scenes.$invalid || isSaving"
            class="btn btn-primary"
          >
            <font-awesome-icon icon="save"></font-awesome-icon>&nbsp;<span v-text="$t('entity.action.save')">Save</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
<script lang="ts" src="./scenes-update.component.ts"></script>
