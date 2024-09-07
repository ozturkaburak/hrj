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

import { getEntities } from './user-assignment.reducer';

export const UserAssignment = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getPaginationState(pageLocation, ITEMS_PER_PAGE, 'id'), pageLocation.search),
  );

  const userAssignmentList = useAppSelector(state => state.hr.userAssignment.entities);
  const loading = useAppSelector(state => state.hr.userAssignment.loading);
  const totalItems = useAppSelector(state => state.hr.userAssignment.totalItems);

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
      <h2 id="user-assignment-heading" data-cy="UserAssignmentHeading">
        <Translate contentKey="hrApp.userAssignment.home.title">User Assignments</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="hrApp.userAssignment.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/user-assignment/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="hrApp.userAssignment.home.createLabel">Create new User Assignment</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {userAssignmentList && userAssignmentList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="hrApp.userAssignment.id">ID</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('orderOfQuestions')}>
                  <Translate contentKey="hrApp.userAssignment.orderOfQuestions">Order Of Questions</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('orderOfQuestions')} />
                </th>
                <th className="hand" onClick={sort('totalDurationInMins')}>
                  <Translate contentKey="hrApp.userAssignment.totalDurationInMins">Total Duration In Mins</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('totalDurationInMins')} />
                </th>
                <th className="hand" onClick={sort('accessUrl')}>
                  <Translate contentKey="hrApp.userAssignment.accessUrl">Access Url</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('accessUrl')} />
                </th>
                <th className="hand" onClick={sort('accessExpiryDate')}>
                  <Translate contentKey="hrApp.userAssignment.accessExpiryDate">Access Expiry Date</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('accessExpiryDate')} />
                </th>
                <th className="hand" onClick={sort('userAssignmentStatus')}>
                  <Translate contentKey="hrApp.userAssignment.userAssignmentStatus">User Assignment Status</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('userAssignmentStatus')} />
                </th>
                <th className="hand" onClick={sort('assignedAt')}>
                  <Translate contentKey="hrApp.userAssignment.assignedAt">Assigned At</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('assignedAt')} />
                </th>
                <th className="hand" onClick={sort('joinedAt')}>
                  <Translate contentKey="hrApp.userAssignment.joinedAt">Joined At</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('joinedAt')} />
                </th>
                <th className="hand" onClick={sort('finishedAt')}>
                  <Translate contentKey="hrApp.userAssignment.finishedAt">Finished At</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('finishedAt')} />
                </th>
                <th>
                  <Translate contentKey="hrApp.userAssignment.user">User</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="hrApp.userAssignment.assignment">Assignment</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {userAssignmentList.map((userAssignment, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/user-assignment/${userAssignment.id}`} color="link" size="sm">
                      {userAssignment.id}
                    </Button>
                  </td>
                  <td>{userAssignment.orderOfQuestions}</td>
                  <td>{userAssignment.totalDurationInMins}</td>
                  <td>{userAssignment.accessUrl}</td>
                  <td>
                    {userAssignment.accessExpiryDate ? (
                      <TextFormat type="date" value={userAssignment.accessExpiryDate} format={APP_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>
                    <Translate contentKey={`hrApp.UserAssignmentStatus.${userAssignment.userAssignmentStatus}`} />
                  </td>
                  <td>
                    {userAssignment.assignedAt ? (
                      <TextFormat type="date" value={userAssignment.assignedAt} format={APP_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>
                    {userAssignment.joinedAt ? <TextFormat type="date" value={userAssignment.joinedAt} format={APP_DATE_FORMAT} /> : null}
                  </td>
                  <td>
                    {userAssignment.finishedAt ? (
                      <TextFormat type="date" value={userAssignment.finishedAt} format={APP_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>{userAssignment.user ? userAssignment.user.id : ''}</td>
                  <td>
                    {userAssignment.assignment ? (
                      <Link to={`/assignment/${userAssignment.assignment.id}`}>{userAssignment.assignment.id}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/user-assignment/${userAssignment.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/user-assignment/${userAssignment.id}/edit?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`}
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
                          (window.location.href = `/user-assignment/${userAssignment.id}/delete?page=${paginationState.activePage}&sort=${paginationState.sort},${paginationState.order}`)
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
              <Translate contentKey="hrApp.userAssignment.home.notFound">No User Assignments found</Translate>
            </div>
          )
        )}
      </div>
      {totalItems ? (
        <div className={userAssignmentList && userAssignmentList.length > 0 ? '' : 'd-none'}>
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

export default UserAssignment;
