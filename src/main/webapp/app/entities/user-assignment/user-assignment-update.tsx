import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText, UncontrolledTooltip } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IUser } from 'app/shared/model/user.model';
import { getUsers } from 'app/modules/administration/user-management/user-management.reducer';
import { IAssignment } from 'app/shared/model/assignment.model';
import { getEntities as getAssignments } from 'app/entities/assignment/assignment.reducer';
import { IUserAssignment } from 'app/shared/model/user-assignment.model';
import { UserAssignmentStatus } from 'app/shared/model/enumerations/user-assignment-status.model';
import { getEntity, updateEntity, createEntity, reset } from './user-assignment.reducer';

export const UserAssignmentUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const users = useAppSelector(state => state.userManagement.users);
  const assignments = useAppSelector(state => state.hr.assignment.entities);
  const userAssignmentEntity = useAppSelector(state => state.hr.userAssignment.entity);
  const loading = useAppSelector(state => state.hr.userAssignment.loading);
  const updating = useAppSelector(state => state.hr.userAssignment.updating);
  const updateSuccess = useAppSelector(state => state.hr.userAssignment.updateSuccess);
  const userAssignmentStatusValues = Object.keys(UserAssignmentStatus);

  const handleClose = () => {
    navigate('/user-assignment' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getUsers({}));
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
    if (values.totalDurationInMins !== undefined && typeof values.totalDurationInMins !== 'number') {
      values.totalDurationInMins = Number(values.totalDurationInMins);
    }
    values.accessExpiryDate = convertDateTimeToServer(values.accessExpiryDate);
    values.assignedAt = convertDateTimeToServer(values.assignedAt);
    values.joinedAt = convertDateTimeToServer(values.joinedAt);
    values.finishedAt = convertDateTimeToServer(values.finishedAt);

    const entity = {
      ...userAssignmentEntity,
      ...values,
      user: users.find(it => it.id.toString() === values.user?.toString()),
      assignment: assignments.find(it => it.id.toString() === values.assignment?.toString()),
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
          accessExpiryDate: displayDefaultDateTime(),
          assignedAt: displayDefaultDateTime(),
          joinedAt: displayDefaultDateTime(),
          finishedAt: displayDefaultDateTime(),
        }
      : {
          userAssignmentStatus: 'NOT_STARTED',
          ...userAssignmentEntity,
          accessExpiryDate: convertDateTimeFromServer(userAssignmentEntity.accessExpiryDate),
          assignedAt: convertDateTimeFromServer(userAssignmentEntity.assignedAt),
          joinedAt: convertDateTimeFromServer(userAssignmentEntity.joinedAt),
          finishedAt: convertDateTimeFromServer(userAssignmentEntity.finishedAt),
          user: userAssignmentEntity?.user?.id,
          assignment: userAssignmentEntity?.assignment?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="hrApp.userAssignment.home.createOrEditLabel" data-cy="UserAssignmentCreateUpdateHeading">
            <Translate contentKey="hrApp.userAssignment.home.createOrEditLabel">Create or edit a UserAssignment</Translate>
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
                  id="user-assignment-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('hrApp.userAssignment.orderOfQuestions')}
                id="user-assignment-orderOfQuestions"
                name="orderOfQuestions"
                data-cy="orderOfQuestions"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <UncontrolledTooltip target="orderOfQuestionsLabel">
                <Translate contentKey="hrApp.userAssignment.help.orderOfQuestions" />
              </UncontrolledTooltip>
              <ValidatedField
                label={translate('hrApp.userAssignment.totalDurationInMins')}
                id="user-assignment-totalDurationInMins"
                name="totalDurationInMins"
                data-cy="totalDurationInMins"
                type="text"
              />
              <UncontrolledTooltip target="totalDurationInMinsLabel">
                <Translate contentKey="hrApp.userAssignment.help.totalDurationInMins" />
              </UncontrolledTooltip>
              <ValidatedField
                label={translate('hrApp.userAssignment.accessUrl')}
                id="user-assignment-accessUrl"
                name="accessUrl"
                data-cy="accessUrl"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <UncontrolledTooltip target="accessUrlLabel">
                <Translate contentKey="hrApp.userAssignment.help.accessUrl" />
              </UncontrolledTooltip>
              <ValidatedField
                label={translate('hrApp.userAssignment.accessExpiryDate')}
                id="user-assignment-accessExpiryDate"
                name="accessExpiryDate"
                data-cy="accessExpiryDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <UncontrolledTooltip target="accessExpiryDateLabel">
                <Translate contentKey="hrApp.userAssignment.help.accessExpiryDate" />
              </UncontrolledTooltip>
              <ValidatedField
                label={translate('hrApp.userAssignment.userAssignmentStatus')}
                id="user-assignment-userAssignmentStatus"
                name="userAssignmentStatus"
                data-cy="userAssignmentStatus"
                type="select"
              >
                {userAssignmentStatusValues.map(userAssignmentStatus => (
                  <option value={userAssignmentStatus} key={userAssignmentStatus}>
                    {translate('hrApp.UserAssignmentStatus.' + userAssignmentStatus)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('hrApp.userAssignment.assignedAt')}
                id="user-assignment-assignedAt"
                name="assignedAt"
                data-cy="assignedAt"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('hrApp.userAssignment.joinedAt')}
                id="user-assignment-joinedAt"
                name="joinedAt"
                data-cy="joinedAt"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('hrApp.userAssignment.finishedAt')}
                id="user-assignment-finishedAt"
                name="finishedAt"
                data-cy="finishedAt"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                id="user-assignment-user"
                name="user"
                data-cy="user"
                label={translate('hrApp.userAssignment.user')}
                type="select"
              >
                <option value="" key="0" />
                {users
                  ? users.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="user-assignment-assignment"
                name="assignment"
                data-cy="assignment"
                label={translate('hrApp.userAssignment.assignment')}
                type="select"
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
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/user-assignment" replace color="info">
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

export default UserAssignmentUpdate;
