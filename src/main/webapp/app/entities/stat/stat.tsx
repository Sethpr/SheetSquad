import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IStat } from 'app/shared/model/stat.model';
import { getEntities } from './stat.reducer';

export const Stat = () => {
  const dispatch = useAppDispatch();

  const location = useLocation();
  const navigate = useNavigate();

  const statList = useAppSelector(state => state.stat.entities);
  const loading = useAppSelector(state => state.stat.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  return (
    <div>
      <h2 id="stat-heading" data-cy="StatHeading">
        Stats
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} /> Refresh list
          </Button>
          <Link to="/stat/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp; Create a new Stat
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {statList && statList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>ID</th>
                <th>Type</th>
                <th>Pool</th>
                <th>Refrence</th>
                <th>Extra</th>
                <th>Owner</th>
                <th />
              </tr>
            </thead>
            <tbody>
              {statList.map((stat, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/stat/${stat.id}`} color="link" size="sm">
                      {stat.id}
                    </Button>
                  </td>
                  <td>{stat.type}</td>
                  <td>{stat.pool ? <Link to={`/pool/${stat.pool.id}`}>{stat.pool.id}</Link> : ''}</td>
                  <td>{stat.refrence ? <Link to={`/refrence/${stat.refrence.id}`}>{stat.refrence.id}</Link> : ''}</td>
                  <td>
                    {stat.extras
                      ? stat.extras.map((val, j) => (
                          <span key={j}>
                            <Link to={`/extra/${val.id}`}>{val.id}</Link>
                            {j === stat.extras.length - 1 ? '' : ', '}
                          </span>
                        ))
                      : null}
                  </td>
                  <td>
                    {stat.owners
                      ? stat.owners.map((val, j) => (
                          <span key={j}>
                            <Link to={`/character/${val.id}`}>{val.id}</Link>
                            {j === stat.owners.length - 1 ? '' : ', '}
                          </span>
                        ))
                      : null}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/stat/${stat.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button tag={Link} to={`/stat/${stat.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button tag={Link} to={`/stat/${stat.id}/delete`} color="danger" size="sm" data-cy="entityDeleteButton">
                        <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Delete</span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && <div className="alert alert-warning">No Stats found</div>
        )}
      </div>
    </div>
  );
};

export default Stat;
