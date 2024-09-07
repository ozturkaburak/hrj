import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ICompany } from 'app/shared/model/company.model';
import { getEntities as getCompanies } from 'app/entities/company/company.reducer';
import { IJobPosting } from 'app/shared/model/job-posting.model';
import { JobStatus } from 'app/shared/model/enumerations/job-status.model';
import { getEntity, updateEntity, createEntity, reset } from './job-posting.reducer';

export const JobPostingUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const companies = useAppSelector(state => state.hr.company.entities);
  const jobPostingEntity = useAppSelector(state => state.hr.jobPosting.entity);
  const loading = useAppSelector(state => state.hr.jobPosting.loading);
  const updating = useAppSelector(state => state.hr.jobPosting.updating);
  const updateSuccess = useAppSelector(state => state.hr.jobPosting.updateSuccess);
  const jobStatusValues = Object.keys(JobStatus);

  const handleClose = () => {
    navigate('/job-posting' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getCompanies({}));
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
    values.createdDate = convertDateTimeToServer(values.createdDate);
    values.expireDate = convertDateTimeToServer(values.expireDate);

    const entity = {
      ...jobPostingEntity,
      ...values,
      company: companies.find(it => it.id.toString() === values.company?.toString()),
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
          createdDate: displayDefaultDateTime(),
          expireDate: displayDefaultDateTime(),
        }
      : {
          status: 'OPEN',
          ...jobPostingEntity,
          createdDate: convertDateTimeFromServer(jobPostingEntity.createdDate),
          expireDate: convertDateTimeFromServer(jobPostingEntity.expireDate),
          company: jobPostingEntity?.company?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="hrApp.jobPosting.home.createOrEditLabel" data-cy="JobPostingCreateUpdateHeading">
            <Translate contentKey="hrApp.jobPosting.home.createOrEditLabel">Create or edit a JobPosting</Translate>
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
                  id="job-posting-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('hrApp.jobPosting.title')}
                id="job-posting-title"
                name="title"
                data-cy="title"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('hrApp.jobPosting.description')}
                id="job-posting-description"
                name="description"
                data-cy="description"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('hrApp.jobPosting.status')}
                id="job-posting-status"
                name="status"
                data-cy="status"
                type="select"
              >
                {jobStatusValues.map(jobStatus => (
                  <option value={jobStatus} key={jobStatus}>
                    {translate('hrApp.JobStatus.' + jobStatus)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('hrApp.jobPosting.createdDate')}
                id="job-posting-createdDate"
                name="createdDate"
                data-cy="createdDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('hrApp.jobPosting.expireDate')}
                id="job-posting-expireDate"
                name="expireDate"
                data-cy="expireDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                id="job-posting-company"
                name="company"
                data-cy="company"
                label={translate('hrApp.jobPosting.company')}
                type="select"
              >
                <option value="" key="0" />
                {companies
                  ? companies.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/job-posting" replace color="info">
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

export default JobPostingUpdate;
