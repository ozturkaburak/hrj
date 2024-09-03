import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './user-assignment.reducer';

export const UserAssignmentDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const userAssignmentEntity = useAppSelector(state => state.hr.userAssignment.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="userAssignmentDetailsHeading">
          <Translate contentKey="hrApp.userAssignment.detail.title">UserAssignment</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{userAssignmentEntity.id}</dd>
          <dt>
            <span id="assignedAt">
              <Translate contentKey="hrApp.userAssignment.assignedAt">Assigned At</Translate>
            </span>
          </dt>
          <dd>
            {userAssignmentEntity.assignedAt ? (
              <TextFormat value={userAssignmentEntity.assignedAt} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="joinedAt">
              <Translate contentKey="hrApp.userAssignment.joinedAt">Joined At</Translate>
            </span>
          </dt>
          <dd>
            {userAssignmentEntity.joinedAt ? (
              <TextFormat value={userAssignmentEntity.joinedAt} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="finishedAt">
              <Translate contentKey="hrApp.userAssignment.finishedAt">Finished At</Translate>
            </span>
          </dt>
          <dd>
            {userAssignmentEntity.finishedAt ? (
              <TextFormat value={userAssignmentEntity.finishedAt} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <Translate contentKey="hrApp.userAssignment.user">User</Translate>
          </dt>
          <dd>{userAssignmentEntity.user ? userAssignmentEntity.user.id : ''}</dd>
          <dt>
            <Translate contentKey="hrApp.userAssignment.assignment">Assignment</Translate>
          </dt>
          <dd>{userAssignmentEntity.assignment ? userAssignmentEntity.assignment.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/user-assignment" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/user-assignment/${userAssignmentEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default UserAssignmentDetail;
