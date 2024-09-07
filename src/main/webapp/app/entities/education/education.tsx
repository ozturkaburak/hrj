import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, TextFormat, getPaginationState, JhiPagination, JhiItemCount } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortUp, faSortDown } from '@fortawesome/free-solid-svg-icons';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities } from './education.reducer';

export const Education = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getPaginationState(pageLocation, ITEMS_PER_PAGE, 'id'), pageLocation.search),
  );

  const educationList = useAppSelector(state => state.hr.education.entities);
  const loading = useAppSelector(state => state.hr.education.loading);
  const totalItems = useAppSelector(state => state.hr.education.totalItems);

  const getAllEntities = () => {
    dispatch(
      getEntities({
        page: paginationState.activePage - 1,
        size: paginationState.itemsPerPage,
        sort: `${paginationState.sort},${paginationState.order}`,
      }),
    );
  };

  const sortEntities = () => {
    getAllEntities();
    const endURL = `?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`;
    if (pageLocation.search !== endURL) {
      navigate(`${pageLocation.pathname}${endURL}`);
    }
  };

  useEffect(() => {
    sortEntities();
  }, [paginationState.activePage, paginationState.order, paginationState.sort]);

  useEffect(() => {
    const params = new URLSearchParams(pageLocation.search);
    const page = params.get('page');
    const sort = params.get(SORT);
    if (page && sort) {
      const sortSplit = sort.split(',');
      setPaginationState({
        ...paginationState,
        activePage: +page,
        sort: sortSplit[0],
        order: sortSplit[1],
      });
    }
  }, [pageLocation.search]);

  const sort = p => () => {
    setPaginationState({
      ...paginationState,
      order: paginationState.order === ASC ? DESC : ASC,
      sort: p,
    });
  };

  const handlePagination = currentPage =>
    setPaginationState({
      ...paginationState,
      activePage: currentPage,
    });

  const handleSyncList = () => {
    sortEntities();
  };

  const getSortIconByFieldName = (fieldName: string) => {
    const sortFieldName = paginationState.sort;
    const order = paginationState.order;
    if (sortFieldName !== fieldName) {
      return faSort;
    } else {
      return order === ASC ? faSortUp : faSortDown;
    }
  };

  return (
    <div>
      <h2 id="education-heading" data-cy="EducationHeading">
        <Translate contentKey="hrApp.education.home.title">Educations</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="hrApp.education.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/education/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="hrApp.education.home.createLabel">Create new Education</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {educationList && educationList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="hrApp.education.id">ID</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('name')}>
                  <Translate contentKey="hrApp.education.name">Name</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('name')} />
                </th>
                <th className="hand" onClick={sort('faculty')}>
                  <Translate contentKey="hrApp.education.faculty">Faculty</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('faculty')} />
                </th>
                <th className="hand" onClick={sort('level')}>
                  <Translate contentKey="hrApp.education.level">Level</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('level')} />
                </th>
                <th className="hand" onClick={sort('degree')}>
                  <Translate contentKey="hrApp.education.degree">Degree</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('degree')} />
                </th>
                <th className="hand" onClick={sort('startDate')}>
                  <Translate contentKey="hrApp.education.startDate">Start Date</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('startDate')} />
                </th>
                <th className="hand" onClick={sort('endDate')}>
                  <Translate contentKey="hrApp.education.endDate">End Date</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('endDate')} />
                </th>
                <th className="hand" onClick={sort('activities')}>
                  <Translate contentKey="hrApp.education.activities">Activities</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('activities')} />
                </th>
                <th className="hand" onClick={sort('clubs')}>
                  <Translate contentKey="hrApp.education.clubs">Clubs</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('clubs')} />
                </th>
                <th className="hand" onClick={sort('createdAt')}>
                  <Translate contentKey="hrApp.education.createdAt">Created At</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('createdAt')} />
                </th>
                <th className="hand" onClick={sort('updatedAt')}>
                  <Translate contentKey="hrApp.education.updatedAt">Updated At</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('updatedAt')} />
                </th>
                <th className="hand" onClick={sort('deletedAt')}>
                  <Translate contentKey="hrApp.education.deletedAt">Deleted At</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('deletedAt')} />
                </th>
                <th>
                  <Translate contentKey="hrApp.education.userProfile">User Profile</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {educationList.map((education, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/education/${education.id}`} color="link" size="sm">
                      {education.id}
                    </Button>
                  </td>
                  <td>{education.name}</td>
                  <td>{education.faculty}</td>
                  <td>
                    <Translate contentKey={`hrApp.EducationLevel.${education.level}`} />
                  </td>
                  <td>{education.degree}</td>
                  <td>
                    {education.startDate ? <TextFormat type="date" value={education.startDate} format={APP_LOCAL_DATE_FORMAT} /> : null}
                  </td>
                  <td>{education.endDate ? <TextFormat type="date" value={education.endDate} format={APP_DATE_FORMAT} /> : null}</td>
                  <td>{education.activities}</td>
                  <td>{education.clubs}</td>
                  <td>{education.createdAt ? <TextFormat type="date" value={education.createdAt} format={APP_DATE_FORMAT} /> : null}</td>
                  <td>{education.updatedAt ? <TextFormat type="date" value={education.updatedAt} format={APP_DATE_FORMAT} /> : null}</td>
                  <td>{education.deletedAt ? <TextFormat type="date" value={education.deletedAt} format={APP_DATE_FORMAT} /> : null}</td>
                  <td>
                    {education.userProfile ? <Link to={`/user-profile/${education.userProfile.id}`}>{education.userProfile.id}</Link> : ''}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/education/${education.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/education/${education.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
                        color="primary"
                        size="sm"
                        data-cy="entityEditButton"
                      >
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        onClick={() =>
                          (window.location.href = `/education/${education.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`)
                        }
                        color="danger"
                        size="sm"
                        data-cy="entityDeleteButton"
                      >
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="hrApp.education.home.notFound">No Educations found</Translate>
            </div>
          )
        )}
      </div>
      {totalItems ? (
        <div className={educationList && educationList.length > 0 ? '' : 'd-none'}>
          <div className="justify-content-center d-flex">
            <JhiItemCount page={paginationState.activePage} total={totalItems} itemsPerPage={paginationState.itemsPerPage} i18nEnabled />
          </div>
          <div className="justify-content-center d-flex">
            <JhiPagination
              activePage={paginationState.activePage}
              onSelect={handlePagination}
              maxButtons={5}
              itemsPerPage={paginationState.itemsPerPage}
              totalItems={totalItems}
            />
          </div>
        </div>
      ) : (
        ''
      )}
    </div>
  );
};

export default Education;
