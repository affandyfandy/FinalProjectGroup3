import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { ToastService } from '../../../services/toast.service';

@Component({
  selector: 'app-toast',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './toast.component.html',
  styleUrl: './toast.component.scss'
})
export class ToastComponent implements OnInit {
  toast: { message: string, type: 'success' | 'error' | 'warning' } | null = null;

  constructor(private toastService: ToastService) {}

  ngOnInit(): void {
    this.toastService.toast$.subscribe(toast => {
      this.toast = toast;
      if (toast) {
        setTimeout(() => this.clearToast(), 3500);
      }
    });
  }

  clearToast(): void {
    this.toast = null;
  }
}