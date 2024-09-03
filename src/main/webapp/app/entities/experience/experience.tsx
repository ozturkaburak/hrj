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

import { getEntities } from './experience.reducer';

export const Experience = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getPaginationState(pageLocation, ITEMS_PER_PAGE, 'id'), pageLocation.search),
  );

  const experienceList = useAppSelector(state => state.hr.experience.entities);
  const loading = useAppSelector(state => state.hr.experience.loading);
  const totalItems = useAppSelector(state => state.hr.experience.totalItems);

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
      <h2 id="experience-heading" data-cy="ExperienceHeading">
        <Translate contentKey="hrApp.experience.home.title">Experiences</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="hrApp.experience.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/experience/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="hrApp.experience.home.createLabel">Create new Experience</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {experienceList && experienceList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="hrApp.experience.id">ID</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('title')}>
                  <Translate contentKey="hrApp.experience.title">Title</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('title')} />
                </th>
                <th className="hand" onClick={sort('companyName')}>
                  <Translate contentKey="hrApp.experience.companyName">Company Name</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('companyName')} />
                </th>
                <th className="hand" onClick={sort('workType')}>
                  <Translate contentKey="hrApp.experience.workType">Work Type</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('workType')} />
                </th>
                <th className="hand" onClick={sort('contractType')}>
                  <Translate contentKey="hrApp.experience.contractType">Contract Type</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('contractType')} />
                </th>
                <th className="hand" onClick={sort('officeLocation')}>
                  <Translate contentKey="hrApp.experience.officeLocation">Office Location</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('officeLocation')} />
                </th>
                <th className="hand" onClick={sort('startDate')}>
                  <Translate contentKey="hrApp.experience.startDate">Start Date</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('startDate')} />
                </th>
                <th className="hand" onClick={sort('endDate')}>
                  <Translate contentKey="hrApp.experience.endDate">End Date</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('endDate')} />
                </th>
                <th className="hand" onClick={sort('description')}>
                  <Translate contentKey="hrApp.experience.description">Description</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('description')} />
                </th>
                <th className="hand" onClick={sort('createdAt')}>
                  <Translate contentKey="hrApp.experience.createdAt">Created At</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('createdAt')} />
                </th>
                <th className="hand" onClick={sort('updatedAt')}>
                  <Translate contentKey="hrApp.experience.updatedAt">Updated At</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('updatedAt')} />
                </th>
                <th className="hand" onClick={sort('deletedAt')}>
                  <Translate contentKey="hrApp.experience.deletedAt">Deleted At</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('deletedAt')} />
                </th>
                <th>
                  <Translate contentKey="hrApp.experience.userProfile">User Profile</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {experienceList.map((experience, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/experience/${experience.id}`} color="link" size="sm">
                      {experience.id}
                    </Button>
                  </td>
                  <td>{experience.title}</td>
                  <td>{experience.companyName}</td>
                  <td>
                    <Translate contentKey={`hrApp.WorkType.${experience.workType}`} />
                  </td>
                  <td>
                    <Translate contentKey={`hrApp.ContractType.${experience.contractType}`} />
                  </td>
                  <td>{experience.officeLocation}</td>
                  <td>{experience.startDate ? <TextFormat type="date" value={experience.startDate} format={APP_DATE_FORMAT} /> : null}</td>
                  <td>{experience.endDate ? <TextFormat type="date" value={experience.endDate} format={APP_DATE_FORMAT} /> : null}</td>
                  <td>{experience.description}</td>
                  <td>{experience.createdAt ? <TextFormat type="date" value={experience.createdAt} format={APP_DATE_FORMAT} /> : null}</td>
                  <td>{experience.updatedAt ? <TextFormat type="date" value={experience.updatedAt} format={APP_DATE_FORMAT} /> : null}</td>
                  <td>{experience.deletedAt ? <TextFormat type="date" value={experience.deletedAt} format={APP_DATE_FORMAT} /> : null}</td>
                  <td>
                    {experience.userProfile ? (
                      <Link to={`/user-profile/${experience.userProfile.id}`}>{experience.userProfile.id}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/experience/${experience.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/experience/${experience.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
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
                          (window.location.href = `/experience/${experience.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`)
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
              <Translate contentKey="hrApp.experience.home.notFound">No Experiences found</Translate>
            </div>
          )
        )}
      </div>
      {totalItems ? (
        <div className={experienceList && experienceList.length > 0 ? '' : 'd-none'}>
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

export default Experience;
