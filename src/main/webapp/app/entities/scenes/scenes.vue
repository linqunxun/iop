<template>
  <div>
    <h2 id="page-heading" data-cy="ScenesHeading">
      <span v-text="$t('iopApp.scenes.home.title')" id="scenes-heading">Scenes</span>
      <div class="d-flex justify-content-end">
        <button class="btn btn-info mr-2" v-on:click="handleSyncList" :disabled="isFetching">
          <font-awesome-icon icon="sync" :spin="isFetching"></font-awesome-icon>
          <span v-text="$t('iopApp.scenes.home.refreshListLabel')">Refresh List</span>
        </button>
        <router-link :to="{ name: 'ScenesCreate' }" custom v-slot="{ navigate }">
          <button
            @click="navigate"
            id="jh-create-entity"
            data-cy="entityCreateButton"
            class="btn btn-primary jh-create-entity create-scenes"
          >
            <font-awesome-icon icon="plus"></font-awesome-icon>
            <span v-text="$t('iopApp.scenes.home.createLabel')"> Create a new Scenes </span>
          </button>
        </router-link>
      </div>
    </h2>
    <br />
    <div class="alert alert-warning" v-if="!isFetching && scenes && scenes.length === 0">
      <span v-text="$t('iopApp.scenes.home.notFound')">No scenes found</span>
    </div>
    <div class="table-responsive" v-if="scenes && scenes.length > 0">
      <table class="table table-striped" aria-describedby="scenes">
        <thead>
          <tr>
            <th scope="row" v-on:click="changeOrder('id')">
              <span v-text="$t('global.field.id')">ID</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'id'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('name')">
              <span v-text="$t('iopApp.scenes.name')">Name</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'name'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('cover')">
              <span v-text="$t('iopApp.scenes.cover')">Cover</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'cover'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('desc')">
              <span v-text="$t('iopApp.scenes.desc')">Desc</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'desc'"></jhi-sort-indicator>
            </th>
            <th scope="row" v-on:click="changeOrder('dataSpec')">
              <span v-text="$t('iopApp.scenes.dataSpec')">Data Spec</span>
              <jhi-sort-indicator :current-order="propOrder" :reverse="reverse" :field-name="'dataSpec'"></jhi-sort-indicator>
            </th>
            <th scope="row"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="scenes in scenes" :key="scenes.id" data-cy="entityTable">
            <td>
              <router-link :to="{ name: 'ScenesView', params: { scenesId: scenes.id } }">{{ scenes.id }}</router-link>
            </td>
            <td>{{ scenes.name }}</td>
            <td>{{ scenes.cover }}</td>
            <td>{{ scenes.desc }}</td>
            <td>{{ scenes.dataSpec }}</td>
            <td class="text-right">
              <div class="btn-group">
                <router-link :to="{ name: 'ScenesView', params: { scenesId: scenes.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-info btn-sm details" data-cy="entityDetailsButton">
                    <font-awesome-icon icon="eye"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="$t('entity.action.view')">View</span>
                  </button>
                </router-link>
                <router-link :to="{ name: 'ScenesEdit', params: { scenesId: scenes.id } }" custom v-slot="{ navigate }">
                  <button @click="navigate" class="btn btn-primary btn-sm edit" data-cy="entityEditButton">
                    <font-awesome-icon icon="pencil-alt"></font-awesome-icon>
                    <span class="d-none d-md-inline" v-text="$t('entity.action.edit')">Edit</span>
                  </button>
                </router-link>
                <b-button
                  v-on:click="prepareRemove(scenes)"
                  variant="danger"
                  class="btn btn-sm"
                  data-cy="entityDeleteButton"
                  v-b-modal.removeEntity
                >
                  <font-awesome-icon icon="times"></font-awesome-icon>
                  <span class="d-none d-md-inline" v-text="$t('entity.action.delete')">Delete</span>
                </b-button>
              </div>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
    <b-modal ref="removeEntity" id="removeEntity">
      <span slot="modal-title"
        ><span id="iopApp.scenes.delete.question" data-cy="scenesDeleteDialogHeading" v-text="$t('entity.delete.title')"
          >Confirm delete operation</span
        ></span
      >
      <div class="modal-body">
        <p id="jhi-delete-scenes-heading" v-text="$t('iopApp.scenes.delete.question', { id: removeId })">
          Are you sure you want to delete this Scenes?
        </p>
      </div>
      <div slot="modal-footer">
        <button type="button" class="btn btn-secondary" v-text="$t('entity.action.cancel')" v-on:click="closeDialog()">Cancel</button>
        <button
          type="button"
          class="btn btn-primary"
          id="jhi-confirm-delete-scenes"
          data-cy="entityConfirmDeleteButton"
          v-text="$t('entity.action.delete')"
          v-on:click="removeScenes()"
        >
          Delete
        </button>
      </div>
    </b-modal>
    <div v-show="scenes && scenes.length > 0">
      <div class="row justify-content-center">
        <jhi-item-count :page="page" :total="queryCount" :itemsPerPage="itemsPerPage"></jhi-item-count>
      </div>
      <div class="row justify-content-center">
        <b-pagination size="md" :total-rows="totalItems" v-model="page" :per-page="itemsPerPage" :change="loadPage(page)"></b-pagination>
      </div>
    </div>
  </div>
</template>

<script lang="ts" src="./scenes.component.ts"></script>
