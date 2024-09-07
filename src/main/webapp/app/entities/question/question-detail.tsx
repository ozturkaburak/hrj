import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, UncontrolledTooltip, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './question.reducer';

export const QuestionDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const questionEntity = useAppSelector(state => state.hr.question.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="questionDetailsHeading">
          <Translate contentKey="hrApp.question.detail.title">Question</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{questionEntity.id}</dd>
          <dt>
            <span id="content">
              <Translate contentKey="hrApp.question.content">Content</Translate>
            </span>
          </dt>
          <dd>{questionEntity.content}</dd>
          <dt>
            <span id="options">
              <Translate contentKey="hrApp.question.options">Options</Translate>
            </span>
            <UncontrolledTooltip target="options">
              <Translate contentKey="hrApp.question.help.options" />
            </UncontrolledTooltip>
          </dt>
          <dd>{questionEntity.options}</dd>
          <dt>
            <span id="type">
              <Translate contentKey="hrApp.question.type">Type</Translate>
            </span>
          </dt>
          <dd>{questionEntity.type}</dd>
          <dt>
            <span id="correctAnswer">
              <Translate contentKey="hrApp.question.correctAnswer">Correct Answer</Translate>
            </span>
            <UncontrolledTooltip target="correctAnswer">
              <Translate contentKey="hrApp.question.help.correctAnswer" />
            </UncontrolledTooltip>
          </dt>
          <dd>{questionEntity.correctAnswer}</dd>
          <dt>
            <span id="createdAt">
              <Translate contentKey="hrApp.question.createdAt">Created At</Translate>
            </span>
          </dt>
          <dd>{questionEntity.createdAt ? <TextFormat value={questionEntity.createdAt} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="updatedAt">
              <Translate contentKey="hrApp.question.updatedAt">Updated At</Translate>
            </span>
          </dt>
          <dd>{questionEntity.updatedAt ? <TextFormat value={questionEntity.updatedAt} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="deletedAt">
              <Translate contentKey="hrApp.question.deletedAt">Deleted At</Translate>
            </span>
          </dt>
          <dd>{questionEntity.deletedAt ? <TextFormat value={questionEntity.deletedAt} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <Translate contentKey="hrApp.question.assignment">Assignment</Translate>
          </dt>
          <dd>
            {questionEntity.assignments
              ? questionEntity.assignments.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.id}</a>
                    {questionEntity.assignments && i === questionEntity.assignments.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
        </dl>
        <Button tag={Link} to="/question" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/question/${questionEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default QuestionDetail;
