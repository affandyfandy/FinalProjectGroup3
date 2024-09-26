import { Component, OnInit } from '@angular/core';
import { Router, RouterOutlet } from '@angular/router';
import { initFlowbite } from 'flowbite';
import { HeaderComponent } from '../header/header.component';
import { CommonModule } from '@angular/common';
import { FooterComponent } from '../footer/footer.component';
import { ToastComponent } from '../toast/toast.component';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [
    CommonModule,
    RouterOutlet,
    HeaderComponent,
    FooterComponent,
    ToastComponent
  ],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss'
})
export class AppComponent implements OnInit {
  title = 'hotel-client';

  constructor(private router: Router){}

  ngOnInit(): void {
    initFlowbite();
  }

  isAuthPage(): boolean {
    return this.router.url.includes('auth');
  }
  
}
