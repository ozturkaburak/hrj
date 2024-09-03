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
import { IExperience } from 'app/shared/model/experience.model';
import { WorkType } from 'app/shared/model/enumerations/work-type.model';
import { ContractType } from 'app/shared/model/enumerations/contract-type.model';
import { getEntity, updateEntity, createEntity, reset } from './experience.reducer';

export const ExperienceUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const userProfiles = useAppSelector(state => state.hr.userProfile.entities);
  const experienceEntity = useAppSelector(state => state.hr.experience.entity);
  const loading = useAppSelector(state => state.hr.experience.loading);
  const updating = useAppSelector(state => state.hr.experience.updating);
  const updateSuccess = useAppSelector(state => state.hr.experience.updateSuccess);
  const workTypeValues = Object.keys(WorkType);
  const contractTypeValues = Object.keys(ContractType);

  const handleClose = () => {
    navigate('/experience' + location.search);
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
    values.startDate = convertDateTimeToServer(values.startDate);
    values.endDate = convertDateTimeToServer(values.endDate);
    values.createdAt = convertDateTimeToServer(values.createdAt);
    values.updatedAt = convertDateTimeToServer(values.updatedAt);
    values.deletedAt = convertDateTimeToServer(values.deletedAt);

    const entity = {
      ...experienceEntity,
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
          startDate: displayDefaultDateTime(),
          endDate: displayDefaultDateTime(),
          createdAt: displayDefaultDateTime(),
          updatedAt: displayDefaultDateTime(),
          deletedAt: displayDefaultDateTime(),
        }
      : {
          workType: 'HYBRID',
          contractType: 'FULL_TIME',
          ...experienceEntity,
          startDate: convertDateTimeFromServer(experienceEntity.startDate),
          endDate: convertDateTimeFromServer(experienceEntity.endDate),
          createdAt: convertDateTimeFromServer(experienceEntity.createdAt),
          updatedAt: convertDateTimeFromServer(experienceEntity.updatedAt),
          deletedAt: convertDateTimeFromServer(experienceEntity.deletedAt),
          userProfile: experienceEntity?.userProfile?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="hrApp.experience.home.createOrEditLabel" data-cy="ExperienceCreateUpdateHeading">
            <Translate contentKey="hrApp.experience.home.createOrEditLabel">Create or edit a Experience</Translate>
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
                  id="experience-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('hrApp.experience.title')}
                id="experience-title"
                name="title"
                data-cy="title"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('hrApp.experience.companyName')}
                id="experience-companyName"
                name="companyName"
                data-cy="companyName"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('hrApp.experience.workType')}
                id="experience-workType"
                name="workType"
                data-cy="workType"
                type="select"
              >
                {workTypeValues.map(workType => (
                  <option value={workType} key={workType}>
                    {translate('hrApp.WorkType.' + workType)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('hrApp.experience.contractType')}
                id="experience-contractType"
                name="contractType"
                data-cy="contractType"
                type="select"
              >
                {contractTypeValues.map(contractType => (
                  <option value={contractType} key={contractType}>
                    {translate('hrApp.ContractType.' + contractType)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('hrApp.experience.officeLocation')}
                id="experience-officeLocation"
                name="officeLocation"
                data-cy="officeLocation"
                type="text"
              />
              <ValidatedField
                label={translate('hrApp.experience.startDate')}
                id="experience-startDate"
                name="startDate"
                data-cy="startDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('hrApp.experience.endDate')}
                id="experience-endDate"
                name="endDate"
                data-cy="endDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('hrApp.experience.description')}
                id="experience-description"
                name="description"
                data-cy="description"
                type="text"
              />
              <ValidatedField
                label={translate('hrApp.experience.createdAt')}
                id="experience-createdAt"
                name="createdAt"
                data-cy="createdAt"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('hrApp.experience.updatedAt')}
                id="experience-updatedAt"
                name="updatedAt"
                data-cy="updatedAt"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('hrApp.experience.deletedAt')}
                id="experience-deletedAt"
                name="deletedAt"
                data-cy="deletedAt"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                id="experience-userProfile"
                name="userProfile"
                data-cy="userProfile"
                label={translate('hrApp.experience.userProfile')}
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
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/experience" replace color="info">
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

export default ExperienceUpdate;
