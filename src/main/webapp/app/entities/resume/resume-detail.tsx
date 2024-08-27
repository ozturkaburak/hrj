import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, openFile, byteSize, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './resume.reducer';

export const ResumeDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const resumeEntity = useAppSelector(state => state.hr.resume.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="resumeDetailsHeading">
          <Translate contentKey="hrApp.resume.detail.title">Resume</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{resumeEntity.id}</dd>
          <dt>
            <span id="file">
              <Translate contentKey="hrApp.resume.file">File</Translate>
            </span>
          </dt>
          <dd>
            {resumeEntity.file ? (
              <div>
                {resumeEntity.fileContentType ? (
                  <a onClick={openFile(resumeEntity.fileContentType, resumeEntity.file)}>
                    <Translate contentKey="entity.action.open">Open</Translate>&nbsp;
                  </a>
                ) : null}
                <span>
                  {resumeEntity.fileContentType}, {byteSize(resumeEntity.file)}
                </span>
              </div>
            ) : null}
          </dd>
          <dt>
            <span id="fileType">
              <Translate contentKey="hrApp.resume.fileType">File Type</Translate>
            </span>
          </dt>
          <dd>{resumeEntity.fileType}</dd>
          <dt>
            <span id="uploadDate">
              <Translate contentKey="hrApp.resume.uploadDate">Upload Date</Translate>
            </span>
          </dt>
          <dd>{resumeEntity.uploadDate ? <TextFormat value={resumeEntity.uploadDate} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <Translate contentKey="hrApp.resume.user">User</Translate>
          </dt>
          <dd>{resumeEntity.user ? resumeEntity.user.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/resume" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/resume/${resumeEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ResumeDetail;
