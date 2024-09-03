import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './assignment.reducer';

export const AssignmentDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const assignmentEntity = useAppSelector(state => state.hr.assignment.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="assignmentDetailsHeading">
          <Translate contentKey="hrApp.assignment.detail.title">Assignment</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{assignmentEntity.id}</dd>
          <dt>
            <span id="type">
              <Translate contentKey="hrApp.assignment.type">Type</Translate>
            </span>
          </dt>
          <dd>{assignmentEntity.type}</dd>
          <dt>
            <span id="visible">
              <Translate contentKey="hrApp.assignment.visible">Visible</Translate>
            </span>
          </dt>
          <dd>{assignmentEntity.visible ? 'true' : 'false'}</dd>
          <dt>
            <span id="hashtags">
              <Translate contentKey="hrApp.assignment.hashtags">Hashtags</Translate>
            </span>
          </dt>
          <dd>{assignmentEntity.hashtags}</dd>
          <dt>
            <span id="createdAt">
              <Translate contentKey="hrApp.assignment.createdAt">Created At</Translate>
            </span>
          </dt>
          <dd>
            {assignmentEntity.createdAt ? <TextFormat value={assignmentEntity.createdAt} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="updatedAt">
              <Translate contentKey="hrApp.assignment.updatedAt">Updated At</Translate>
            </span>
          </dt>
          <dd>
            {assignmentEntity.updatedAt ? <TextFormat value={assignmentEntity.updatedAt} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="deletedAt">
              <Translate contentKey="hrApp.assignment.deletedAt">Deleted At</Translate>
            </span>
          </dt>
          <dd>
            {assignmentEntity.deletedAt ? <TextFormat value={assignmentEntity.deletedAt} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <Translate contentKey="hrApp.assignment.questions">Questions</Translate>
          </dt>
          <dd>
            {assignmentEntity.questions
              ? assignmentEntity.questions.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.id}</a>
                    {assignmentEntity.questions && i === assignmentEntity.questions.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
        </dl>
        <Button tag={Link} to="/assignment" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/assignment/${assignmentEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default AssignmentDetail;
