import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IGrade, NewGrade } from '../grade.model';

export type PartialUpdateGrade = Partial<IGrade> & Pick<IGrade, 'id'>;

export type EntityResponseType = HttpResponse<IGrade>;
export type EntityArrayResponseType = HttpResponse<IGrade[]>;

@Injectable({ providedIn: 'root' })
export class GradeService {
  protected readonly http = inject(HttpClient);
  protected readonly applicationConfigService = inject(ApplicationConfigService);

  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/grades');

  create(grade: NewGrade): Observable<EntityResponseType> {
    return this.http.post<IGrade>(this.resourceUrl, grade, { observe: 'response' });
  }

  update(grade: IGrade): Observable<EntityResponseType> {
    return this.http.put<IGrade>(`${this.resourceUrl}/${this.getGradeIdentifier(grade)}`, grade, { observe: 'response' });
  }

  partialUpdate(grade: PartialUpdateGrade): Observable<EntityResponseType> {
    return this.http.patch<IGrade>(`${this.resourceUrl}/${this.getGradeIdentifier(grade)}`, grade, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IGrade>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IGrade[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getGradeIdentifier(grade: Pick<IGrade, 'id'>): number {
    return grade.id;
  }

  compareGrade(o1: Pick<IGrade, 'id'> | null, o2: Pick<IGrade, 'id'> | null): boolean {
    return o1 && o2 ? this.getGradeIdentifier(o1) === this.getGradeIdentifier(o2) : o1 === o2;
  }

  addGradeToCollectionIfMissing<Type extends Pick<IGrade, 'id'>>(
    gradeCollection: Type[],
    ...gradesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const grades: Type[] = gradesToCheck.filter(isPresent);
    if (grades.length > 0) {
      const gradeCollectionIdentifiers = gradeCollection.map(gradeItem => this.getGradeIdentifier(gradeItem));
      const gradesToAdd = grades.filter(gradeItem => {
        const gradeIdentifier = this.getGradeIdentifier(gradeItem);
        if (gradeCollectionIdentifiers.includes(gradeIdentifier)) {
          return false;
        }
        gradeCollectionIdentifiers.push(gradeIdentifier);
        return true;
      });
      return [...gradesToAdd, ...gradeCollection];
    }
    return gradeCollection;
  }
}
