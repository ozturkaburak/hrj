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
import { IEducation } from 'app/shared/model/education.model';
import { EducationLevel } from 'app/shared/model/enumerations/education-level.model';
import { getEntity, updateEntity, createEntity, reset } from './education.reducer';

export const EducationUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const userProfiles = useAppSelector(state => state.hr.userProfile.entities);
  const educationEntity = useAppSelector(state => state.hr.education.entity);
  const loading = useAppSelector(state => state.hr.education.loading);
  const updating = useAppSelector(state => state.hr.education.updating);
  const updateSuccess = useAppSelector(state => state.hr.education.updateSuccess);
  const educationLevelValues = Object.keys(EducationLevel);

  const handleClose = () => {
    navigate('/education' + location.search);
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
    values.endDate = convertDateTimeToServer(values.endDate);
    values.createdAt = convertDateTimeToServer(values.createdAt);
    values.updatedAt = convertDateTimeToServer(values.updatedAt);
    values.deletedAt = convertDateTimeToServer(values.deletedAt);

    const entity = {
      ...educationEntity,
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
          endDate: displayDefaultDateTime(),
          createdAt: displayDefaultDateTime(),
          updatedAt: displayDefaultDateTime(),
          deletedAt: displayDefaultDateTime(),
        }
      : {
          level: 'HIGH_SCHOOL',
          ...educationEntity,
          endDate: convertDateTimeFromServer(educationEntity.endDate),
          createdAt: convertDateTimeFromServer(educationEntity.createdAt),
          updatedAt: convertDateTimeFromServer(educationEntity.updatedAt),
          deletedAt: convertDateTimeFromServer(educationEntity.deletedAt),
          userProfile: educationEntity?.userProfile?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="hrApp.education.home.createOrEditLabel" data-cy="EducationCreateUpdateHeading">
            <Translate contentKey="hrApp.education.home.createOrEditLabel">Create or edit a Education</Translate>
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
                  id="education-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('hrApp.education.name')}
                id="education-name"
                name="name"
                data-cy="name"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('hrApp.education.faculty')}
                id="education-faculty"
                name="faculty"
                data-cy="faculty"
                type="text"
              />
              <ValidatedField label={translate('hrApp.education.level')} id="education-level" name="level" data-cy="level" type="select">
                {educationLevelValues.map(educationLevel => (
                  <option value={educationLevel} key={educationLevel}>
                    {translate('hrApp.EducationLevel.' + educationLevel)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('hrApp.education.degree')}
                id="education-degree"
                name="degree"
                data-cy="degree"
                type="text"
              />
              <ValidatedField
                label={translate('hrApp.education.startDate')}
                id="education-startDate"
                name="startDate"
                data-cy="startDate"
                type="date"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('hrApp.education.endDate')}
                id="education-endDate"
                name="endDate"
                data-cy="endDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('hrApp.education.activities')}
                id="education-activities"
                name="activities"
                data-cy="activities"
                type="text"
              />
              <ValidatedField label={translate('hrApp.education.clubs')} id="education-clubs" name="clubs" data-cy="clubs" type="text" />
              <ValidatedField
                label={translate('hrApp.education.createdAt')}
                id="education-createdAt"
                name="createdAt"
                data-cy="createdAt"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('hrApp.education.updatedAt')}
                id="education-updatedAt"
                name="updatedAt"
                data-cy="updatedAt"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('hrApp.education.deletedAt')}
                id="education-deletedAt"
                name="deletedAt"
                data-cy="deletedAt"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                id="education-userProfile"
                name="userProfile"
                data-cy="userProfile"
                label={translate('hrApp.education.userProfile')}
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
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/education" replace color="info">
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

export default EducationUpdate;
