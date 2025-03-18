import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, map } from 'rxjs';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ISubmissionAudit, NewSubmissionAudit } from '../submission-audit.model';

export type PartialUpdateSubmissionAudit = Partial<ISubmissionAudit> & Pick<ISubmissionAudit, 'id'>;

type RestOf<T extends ISubmissionAudit | NewSubmissionAudit> = Omit<T, 'acquisitionDate' | 'lossDate' | 'decisionDate' | 'modifiedDate'> & {
  acquisitionDate?: string | null;
  lossDate?: string | null;
  decisionDate?: string | null;
  modifiedDate?: string | null;
};

export type RestSubmissionAudit = RestOf<ISubmissionAudit>;

export type NewRestSubmissionAudit = RestOf<NewSubmissionAudit>;

export type PartialUpdateRestSubmissionAudit = RestOf<PartialUpdateSubmissionAudit>;

export type EntityResponseType = HttpResponse<ISubmissionAudit>;
export type EntityArrayResponseType = HttpResponse<ISubmissionAudit[]>;

@Injectable({ providedIn: 'root' })
export class SubmissionAuditService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/submission-audits');

  create(submissionAudit: NewSubmissionAudit): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(submissionAudit);
    return this.http
      .post<RestSubmissionAudit>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(submissionAudit: ISubmissionAudit): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(submissionAudit);
    return this.http
      .put<RestSubmissionAudit>(`${this.resourceUrl}/${this.getSubmissionAuditIdentifier(submissionAudit)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(submissionAudit: PartialUpdateSubmissionAudit): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(submissionAudit);
    return this.http
      .patch<RestSubmissionAudit>(`${this.resourceUrl}/${this.getSubmissionAuditIdentifier(submissionAudit)}`, copy, {
        observe: 'response',
      })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestSubmissionAudit>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestSubmissionAudit[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getSubmissionAuditIdentifier(submissionAudit: Pick<ISubmissionAudit, 'id'>): number {
    return submissionAudit.id;
  }

  compareSubmissionAudit(o1: Pick<ISubmissionAudit, 'id'> | null, o2: Pick<ISubmissionAudit, 'id'> | null): boolean {
    return o1 && o2 ? this.getSubmissionAuditIdentifier(o1) === this.getSubmissionAuditIdentifier(o2) : o1 === o2;
  }

  addSubmissionAuditToCollectionIfMissing<Type extends Pick<ISubmissionAudit, 'id'>>(
    submissionAuditCollection: Type[],
    ...submissionAuditsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const submissionAudits: Type[] = submissionAuditsToCheck.filter(isPresent);
    if (submissionAudits.length > 0) {
      const submissionAuditCollectionIdentifiers = submissionAuditCollection.map(submissionAuditItem =>
        this.getSubmissionAuditIdentifier(submissionAuditItem),
      );
      const submissionAuditsToAdd = submissionAudits.filter(submissionAuditItem => {
        const submissionAuditIdentifier = this.getSubmissionAuditIdentifier(submissionAuditItem);
        if (submissionAuditCollectionIdentifiers.includes(submissionAuditIdentifier)) {
          return false;
        }
        submissionAuditCollectionIdentifiers.push(submissionAuditIdentifier);
        return true;
      });
      return [...submissionAuditsToAdd, ...submissionAuditCollection];
    }
    return submissionAuditCollection;
  }

  protected convertDateFromClient<T extends ISubmissionAudit | NewSubmissionAudit | PartialUpdateSubmissionAudit>(
    submissionAudit: T,
  ): RestOf<T> {
    return {
      ...submissionAudit,
      acquisitionDate: submissionAudit.acquisitionDate?.format(DATE_FORMAT) ?? null,
      lossDate: submissionAudit.lossDate?.format(DATE_FORMAT) ?? null,
      decisionDate: submissionAudit.decisionDate?.format(DATE_FORMAT) ?? null,
      modifiedDate: submissionAudit.modifiedDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restSubmissionAudit: RestSubmissionAudit): ISubmissionAudit {
    return {
      ...restSubmissionAudit,
      acquisitionDate: restSubmissionAudit.acquisitionDate ? dayjs(restSubmissionAudit.acquisitionDate) : undefined,
      lossDate: restSubmissionAudit.lossDate ? dayjs(restSubmissionAudit.lossDate) : undefined,
      decisionDate: restSubmissionAudit.decisionDate ? dayjs(restSubmissionAudit.decisionDate) : undefined,
      modifiedDate: restSubmissionAudit.modifiedDate ? dayjs(restSubmissionAudit.modifiedDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestSubmissionAudit>): HttpResponse<ISubmissionAudit> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestSubmissionAudit[]>): HttpResponse<ISubmissionAudit[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
