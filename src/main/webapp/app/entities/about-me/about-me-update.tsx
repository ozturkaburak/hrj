import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IUserProfile } from 'app/shared/model/user-profile.model';
import { getEntities as getUserProfiles } from 'app/entities/user-profile/user-profile.reducer';
import { IAboutMe } from 'app/shared/model/about-me.model';
import { SocialMediaType } from 'app/shared/model/enumerations/social-media-type.model';
import { getEntity, updateEntity, createEntity, reset } from './about-me.reducer';

export const AboutMeUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const userProfiles = useAppSelector(state => state.hr.userProfile.entities);
  const aboutMeEntity = useAppSelector(state => state.hr.aboutMe.entity);
  const loading = useAppSelector(state => state.hr.aboutMe.loading);
  const updating = useAppSelector(state => state.hr.aboutMe.updating);
  const updateSuccess = useAppSelector(state => state.hr.aboutMe.updateSuccess);
  const socialMediaTypeValues = Object.keys(SocialMediaType);

  const handleClose = () => {
    navigate('/about-me' + location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getUserProfiles({}));
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
      ...aboutMeEntity,
      ...values,
      userProfile: userProfiles.find(it => it.id.toString() === values.userProfile?.toString()),
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
          socialMedia: 'INSTAGRAM',
          ...aboutMeEntity,
          createdAt: convertDateTimeFromServer(aboutMeEntity.createdAt),
          updatedAt: convertDateTimeFromServer(aboutMeEntity.updatedAt),
          deletedAt: convertDateTimeFromServer(aboutMeEntity.deletedAt),
          userProfile: aboutMeEntity?.userProfile?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="hrApp.aboutMe.home.createOrEditLabel" data-cy="AboutMeCreateUpdateHeading">
            <Translate contentKey="hrApp.aboutMe.home.createOrEditLabel">Create or edit a AboutMe</Translate>
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
                  id="about-me-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('hrApp.aboutMe.socialMedia')}
                id="about-me-socialMedia"
                name="socialMedia"
                data-cy="socialMedia"
                type="select"
              >
                {socialMediaTypeValues.map(socialMediaType => (
                  <option value={socialMediaType} key={socialMediaType}>
                    {translate('hrApp.SocialMediaType.' + socialMediaType)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('hrApp.aboutMe.url')}
                id="about-me-url"
                name="url"
                data-cy="url"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('hrApp.aboutMe.createdAt')}
                id="about-me-createdAt"
                name="createdAt"
                data-cy="createdAt"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('hrApp.aboutMe.updatedAt')}
                id="about-me-updatedAt"
                name="updatedAt"
                data-cy="updatedAt"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('hrApp.aboutMe.deletedAt')}
                id="about-me-deletedAt"
                name="deletedAt"
                data-cy="deletedAt"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                id="about-me-userProfile"
                name="userProfile"
                data-cy="userProfile"
                label={translate('hrApp.aboutMe.userProfile')}
                type="select"
              >
                <option value="" key="0" />
                {userProfiles
                  ? userProfiles.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/about-me" replace color="info">
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

export default AboutMeUpdate;
