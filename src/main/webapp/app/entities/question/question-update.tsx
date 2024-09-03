import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IAssignment } from 'app/shared/model/assignment.model';
import { getEntities as getAssignments } from 'app/entities/assignment/assignment.reducer';
import { IQuestion } from 'app/shared/model/question.model';
import { QuestionType } from 'app/shared/model/enumerations/question-type.model';
import { getEntity, updateEntity, createEntity, reset } from './question.reducer';

export const QuestionUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const assignments = useAppSelector(state => state.hr.assignment.entities);
  const questionEntity = useAppSelector(state => state.hr.question.entity);
  const loading = useAppSelector(state => state.hr.question.loading);
  const updating = useAppSelector(state => state.hr.question.updating);
  const updateSuccess = useAppSelector(state => state.hr.question.updateSuccess);
  const questionTypeValues = Object.keys(QuestionType);

  const handleClose = () => {
    navigate('/question' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getAssignments({}));
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
      ...questionEntity,
      ...values,
      assignments: mapIdList(values.assignments),
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
          type: 'MULTIPLE_CHOICE',
          ...questionEntity,
          createdAt: convertDateTimeFromServer(questionEntity.createdAt),
          updatedAt: convertDateTimeFromServer(questionEntity.updatedAt),
          deletedAt: convertDateTimeFromServer(questionEntity.deletedAt),
          assignments: questionEntity?.assignments?.map(e => e.id.toString()),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="hrApp.question.home.createOrEditLabel" data-cy="QuestionCreateUpdateHeading">
            <Translate contentKey="hrApp.question.home.createOrEditLabel">Create or edit a Question</Translate>
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
                  id="question-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('hrApp.question.content')}
                id="question-content"
                name="content"
                data-cy="content"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('hrApp.question.options')}
                id="question-options"
                name="options"
                data-cy="options"
                type="text"
              />
              <ValidatedField label={translate('hrApp.question.type')} id="question-type" name="type" data-cy="type" type="select">
                {questionTypeValues.map(questionType => (
                  <option value={questionType} key={questionType}>
                    {translate('hrApp.QuestionType.' + questionType)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('hrApp.question.correctAnswer')}
                id="question-correctAnswer"
                name="correctAnswer"
                data-cy="correctAnswer"
                type="text"
              />
              <ValidatedField
                label={translate('hrApp.question.createdAt')}
                id="question-createdAt"
                name="createdAt"
                data-cy="createdAt"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('hrApp.question.updatedAt')}
                id="question-updatedAt"
                name="updatedAt"
                data-cy="updatedAt"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('hrApp.question.deletedAt')}
                id="question-deletedAt"
                name="deletedAt"
                data-cy="deletedAt"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('hrApp.question.assignment')}
                id="question-assignment"
                data-cy="assignment"
                type="select"
                multiple
                name="assignments"
              >
                <option value="" key="0" />
                {assignments
                  ? assignments.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/question" replace color="info">
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

export default QuestionUpdate;
