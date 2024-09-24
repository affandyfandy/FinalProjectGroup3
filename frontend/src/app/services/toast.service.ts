import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ToastService {
  private toastSubject = new BehaviorSubject<{ message: string, type: 'success' | 'error' | 'warning' } | null>(null);
  toast$ = this.toastSubject.asObservable();

  showToast(message: string, type: 'success' | 'error' | 'warning' = 'warning') {
    this.toastSubject.next({ message, type });
    setTimeout(() => this.toastSubject.next(null), 5000);
  }
}