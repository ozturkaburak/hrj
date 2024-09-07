import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IUserProfile } from 'app/shared/model/user-profile.model';
import { getEntities as getUserProfiles } from 'app/entities/user-profile/user-profile.reducer';
import { IUpload } from 'app/shared/model/upload.model';
import { FileType } from 'app/shared/model/enumerations/file-type.model';
import { FileExtention } from 'app/shared/model/enumerations/file-extention.model';
import { getEntity, updateEntity, createEntity, reset } from './upload.reducer';

export const UploadUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const userProfiles = useAppSelector(state => state.hr.userProfile.entities);
  const uploadEntity = useAppSelector(state => state.hr.upload.entity);
  const loading = useAppSelector(state => state.hr.upload.loading);
  const updating = useAppSelector(state => state.hr.upload.updating);
  const updateSuccess = useAppSelector(state => state.hr.upload.updateSuccess);
  const fileTypeValues = Object.keys(FileType);
  const fileExtentionValues = Object.keys(FileExtention);

  const handleClose = () => {
    navigate('/upload' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getUserProfiles({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  // eslint-disable-next-line complexity
  const saveEntity = values => {
    if (values.id !== undefined && typeof values.id !== 'number') {
      values.id = Number(values.id);
    }
    values.uploadDate = convertDateTimeToServer(values.uploadDate);

    const entity = {
      ...uploadEntity,
      ...values,
      userProfile: userProfiles.find(it => it.id.toString() === values.userProfile?.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {
          uploadDate: displayDefaultDateTime(),
        }
      : {
          type: 'CV',
          extension: 'PDF',
          ...uploadEntity,
          uploadDate: convertDateTimeFromServer(uploadEntity.uploadDate),
          userProfile: uploadEntity?.userProfile?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="hrApp.upload.home.createOrEditLabel" data-cy="UploadCreateUpdateHeading">
            <Translate contentKey="hrApp.upload.home.createOrEditLabel">Create or edit a Upload</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="upload-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('hrApp.upload.url')}
                id="upload-url"
                name="url"
                data-cy="url"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField label={translate('hrApp.upload.type')} id="upload-type" name="type" data-cy="type" type="select">
                {fileTypeValues.map(fileType => (
                  <option value={fileType} key={fileType}>
                    {translate('hrApp.FileType.' + fileType)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('hrApp.upload.extension')}
                id="upload-extension"
                name="extension"
                data-cy="extension"
                type="select"
              >
                {fileExtentionValues.map(fileExtention => (
                  <option value={fileExtention} key={fileExtention}>
                    {translate('hrApp.FileExtention.' + fileExtention)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('hrApp.upload.uploadDate')}
                id="upload-uploadDate"
                name="uploadDate"
                data-cy="uploadDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                id="upload-userProfile"
                name="userProfile"
                data-cy="userProfile"
                label={translate('hrApp.upload.userProfile')}
                type="select"
              >
                <option value="" key="0" />
                {userProfiles
                  ? userProfiles.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/upload" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default UploadUpdate;
