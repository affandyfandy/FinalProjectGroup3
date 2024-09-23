import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-not-found',
  standalone: true,
  imports: [
    CommonModule
  ],
  templateUrl: './not-found.component.html'
})
export class NotFoundComponent{
  constructor(
    private router: Router
  ){}

  back(): void{
    this.router.navigate(['/']);
  }

}
