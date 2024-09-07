import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './job-posting.reducer';

export const JobPostingDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const jobPostingEntity = useAppSelector(state => state.hr.jobPosting.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="jobPostingDetailsHeading">
          <Translate contentKey="hrApp.jobPosting.detail.title">JobPosting</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{jobPostingEntity.id}</dd>
          <dt>
            <span id="title">
              <Translate contentKey="hrApp.jobPosting.title">Title</Translate>
            </span>
          </dt>
          <dd>{jobPostingEntity.title}</dd>
          <dt>
            <span id="description">
              <Translate contentKey="hrApp.jobPosting.description">Description</Translate>
            </span>
          </dt>
          <dd>{jobPostingEntity.description}</dd>
          <dt>
            <span id="status">
              <Translate contentKey="hrApp.jobPosting.status">Status</Translate>
            </span>
          </dt>
          <dd>{jobPostingEntity.status}</dd>
          <dt>
            <span id="createdDate">
              <Translate contentKey="hrApp.jobPosting.createdDate">Created Date</Translate>
            </span>
          </dt>
          <dd>
            {jobPostingEntity.createdDate ? <TextFormat value={jobPostingEntity.createdDate} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="expireDate">
              <Translate contentKey="hrApp.jobPosting.expireDate">Expire Date</Translate>
            </span>
          </dt>
          <dd>
            {jobPostingEntity.expireDate ? <TextFormat value={jobPostingEntity.expireDate} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <Translate contentKey="hrApp.jobPosting.company">Company</Translate>
          </dt>
          <dd>{jobPostingEntity.company ? jobPostingEntity.company.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/job-posting" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/job-posting/${jobPostingEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default JobPostingDetail;
