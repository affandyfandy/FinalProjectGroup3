import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-unauthorized',
  standalone: true,
  imports: [
    CommonModule
  ],
  templateUrl: './unauthorized.component.html'
})
export class UnauthorizedComponent {
  constructor(
    private router: Router
  ){}

  back(): void{
    this.router.navigate(['/']);
  }
}
