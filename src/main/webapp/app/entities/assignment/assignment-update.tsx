import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IQuestion } from 'app/shared/model/question.model';
import { getEntities as getQuestions } from 'app/entities/question/question.reducer';
import { IAssignment } from 'app/shared/model/assignment.model';
import { AssignmentType } from 'app/shared/model/enumerations/assignment-type.model';
import { getEntity, updateEntity, createEntity, reset } from './assignment.reducer';

export const AssignmentUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const questions = useAppSelector(state => state.hr.question.entities);
  const assignmentEntity = useAppSelector(state => state.hr.assignment.entity);
  const loading = useAppSelector(state => state.hr.assignment.loading);
  const updating = useAppSelector(state => state.hr.assignment.updating);
  const updateSuccess = useAppSelector(state => state.hr.assignment.updateSuccess);
  const assignmentTypeValues = Object.keys(AssignmentType);

  const handleClose = () => {
    navigate('/assignment' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getQuestions({}));
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
    values.createdAt = convertDateTimeToServer(values.createdAt);
    values.updatedAt = convertDateTimeToServer(values.updatedAt);
    values.deletedAt = convertDateTimeToServer(values.deletedAt);

    const entity = {
      ...assignmentEntity,
      ...values,
      questions: mapIdList(values.questions),
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
          createdAt: displayDefaultDateTime(),
          updatedAt: displayDefaultDateTime(),
          deletedAt: displayDefaultDateTime(),
        }
      : {
          type: 'TECHNICAL',
          ...assignmentEntity,
          createdAt: convertDateTimeFromServer(assignmentEntity.createdAt),
          updatedAt: convertDateTimeFromServer(assignmentEntity.updatedAt),
          deletedAt: convertDateTimeFromServer(assignmentEntity.deletedAt),
          questions: assignmentEntity?.questions?.map(e => e.id.toString()),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="hrApp.assignment.home.createOrEditLabel" data-cy="AssignmentCreateUpdateHeading">
            <Translate contentKey="hrApp.assignment.home.createOrEditLabel">Create or edit a Assignment</Translate>
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
                  id="assignment-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField label={translate('hrApp.assignment.type')} id="assignment-type" name="type" data-cy="type" type="select">
                {assignmentTypeValues.map(assignmentType => (
                  <option value={assignmentType} key={assignmentType}>
                    {translate('hrApp.AssignmentType.' + assignmentType)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('hrApp.assignment.visible')}
                id="assignment-visible"
                name="visible"
                data-cy="visible"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('hrApp.assignment.hashtags')}
                id="assignment-hashtags"
                name="hashtags"
                data-cy="hashtags"
                type="text"
              />
              <ValidatedField
                label={translate('hrApp.assignment.createdAt')}
                id="assignment-createdAt"
                name="createdAt"
                data-cy="createdAt"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('hrApp.assignment.updatedAt')}
                id="assignment-updatedAt"
                name="updatedAt"
                data-cy="updatedAt"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('hrApp.assignment.deletedAt')}
                id="assignment-deletedAt"
                name="deletedAt"
                data-cy="deletedAt"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('hrApp.assignment.questions')}
                id="assignment-questions"
                data-cy="questions"
                type="select"
                multiple
                name="questions"
              >
                <option value="" key="0" />
                {questions
                  ? questions.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/assignment" replace color="info">
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

export default AssignmentUpdate;
