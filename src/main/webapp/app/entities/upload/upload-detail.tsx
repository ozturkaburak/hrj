import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, openFile, byteSize, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './upload.reducer';

export const UploadDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const uploadEntity = useAppSelector(state => state.hr.upload.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="uploadDetailsHeading">
          <Translate contentKey="hrApp.upload.detail.title">Upload</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{uploadEntity.id}</dd>
          <dt>
            <span id="file">
              <Translate contentKey="hrApp.upload.file">File</Translate>
            </span>
          </dt>
          <dd>
            {uploadEntity.file ? (
              <div>
                {uploadEntity.fileContentType ? (
                  <a onClick={openFile(uploadEntity.fileContentType, uploadEntity.file)}>
                    <Translate contentKey="entity.action.open">Open</Translate>&nbsp;
                  </a>
                ) : null}
                <span>
                  {uploadEntity.fileContentType}, {byteSize(uploadEntity.file)}
                </span>
              </div>
            ) : null}
          </dd>
          <dt>
            <span id="fileType">
              <Translate contentKey="hrApp.upload.fileType">File Type</Translate>
            </span>
          </dt>
          <dd>{uploadEntity.fileType}</dd>
          <dt>
            <span id="uploadDate">
              <Translate contentKey="hrApp.upload.uploadDate">Upload Date</Translate>
            </span>
          </dt>
          <dd>{uploadEntity.uploadDate ? <TextFormat value={uploadEntity.uploadDate} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <Translate contentKey="hrApp.upload.userProfile">User Profile</Translate>
          </dt>
          <dd>{uploadEntity.userProfile ? uploadEntity.userProfile.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/upload" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/upload/${uploadEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default UploadDetail;
