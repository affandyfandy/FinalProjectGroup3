import { CommonModule } from '@angular/common';
import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Facility, Room, RoomType, Status } from '../../../../model/room.model';
import { RoomService } from '../../../../services/room.service';
import { NgIconComponent, provideIcons } from '@ng-icons/core';
import { heroChevronLeft } from '@ng-icons/heroicons/outline';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { ToastService } from '../../../../services/toast.service';
import { HttpEvent, HttpEventType } from '@angular/common/http';

@Component({
  selector: 'app-room-form',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    NgIconComponent,
    RouterModule
  ],
  templateUrl: './room-form.component.html',
  providers: [
    provideIcons({ heroChevronLeft })
  ]
})
export class RoomFormComponent implements OnInit {
  @Input() room: Room | null = null;
  @Input() action?: string;
  @Output() save = new EventEmitter<Room>();
  @Output() cancel = new EventEmitter<void>();

  roomId?: string | null = null;
  roomTypes = Object.values(RoomType); 
  selectedRoomType: RoomType = RoomType.SINGLE; 
  roomStatus = Object.values(Status); 
  selectedRoomStatus: Status = Status.ACTIVE; 
  roomFacility = Object.values(Facility);

  selectedFacility: Facility[] = [];
  facilityList: string[] = [];

  roomForm: FormGroup;
  isVisible = true;
  showFileUpload = false;
  selectedFile: File | null = null;
  message: string | null = null;

  constructor(
    private fb: FormBuilder,
    private roomService: RoomService,
    private route: ActivatedRoute,
    private router: Router,
    private toastService: ToastService) {
    this.roomForm = this.fb.group({
      roomType: ['',[Validators.required]],
      roomNumber: ['',[Validators.required]],
      capacity: ['',[Validators.required, Validators.pattern('^[0-9]+$')]],
      status: ['',[Validators.required]],
      price: ['', [Validators.required, Validators.pattern('^[0-9]+$')]],
      photo: [''],
      facility: [[]]
    });
  }
  
  ngOnInit(): void {
    console.log(this.route.snapshot.url);
    const currentRoute = this.route.snapshot.url[this.route.snapshot.url.length - 1]?.path;
    console.log(currentRoute);
    this.action = currentRoute === 'edit' ? 'Edit Room' : (currentRoute === 'create' ? 'Add New Room' : 'Room Details');
    this.roomId = this.route.snapshot.paramMap.get('id');
    this.loadRoom();
  }

  loadRoom(): void {
    if (this.roomId) {
      this.roomService.getRoomById(this.roomId).subscribe({
        next: (room: Room) => {
          this.room = room;
          this.roomForm.patchValue({
            roomType: this.room.roomType,
            roomNumber: this.room.roomNumber,
            capacity: this.room.capacity,
            status: this.room.status,
            price: this.room.price,
            photo: this.room.photo,
            facility: this.room.facility,
          });
          this.updateSelectedFacilities();
        },
        error: (err) => {
          console.error('Error fetching room:', err);
          this.message = err;
        }
      });
    }
  }

  updateSelectedFacilities(): void {
    if (this.room?.facility) {
      this.selectedFacility = this.room.facility.map((facility: Facility) => facility);
    } else {
      this.selectedFacility = [];
    }
    // console.log(this.selectedFacility);

    this.selectedFacility.forEach((facility) => {
      if (facility.toString() === 'TELEVISION'){
        this.facilityList.push('Television');
      }
      else if (facility.toString() === 'REFRIGERATOR'){
        this.facilityList.push('Refrigerator');
      }
      else if (facility.toString() === 'MINIBAR'){
        this.facilityList.push('Minibar');
      }
      else if (facility.toString() === 'WIFI'){
        this.facilityList.push('Wi-Fi');
      }
      else if (facility.toString() === 'COFFEE_MAKER'){
        this.facilityList.push('Coffee Maker');
      }
      else if (facility.toString() === 'HAIR_DRYER'){
        this.facilityList.push('Hair Dryer');
      }
    })
    console.log(this.facilityList);
    console.log("kesini dulu");
    console.log(this.roomFacility);
  }

  onSubmit(): void {
    if (this.roomForm.valid) {
      const roomData = {
        ...this.roomForm.value
      };

      console.log(roomData);
      if (this.action === 'Edit Room' && this.roomId) {
        this.roomService.editRoomData(this.roomId, roomData).subscribe({
          next: (updatedRoom) => {
            console.log('Room updated successfully:', updatedRoom);
            this.save.emit(updatedRoom);
            this.toastService.showToast('Room updated successfully!', 'success');
          },
          error: (err) => {
            console.error('Error updating room:', err);
            this.toastService.showToast('Error updating room: ' + err, 'error');
          }
        });
      } else if (this.action === 'Add New Room') {
        this.roomService.createRoom(roomData).subscribe({
          next: (createdRoom) => {
            console.log('Room created successfully:', createdRoom);
            this.save.emit(createdRoom);
            this.toastService.showToast('Room created successfully!', 'success');
          },
          error: (err) => {
            console.error('Error creating room:', err);
            this.message = err;
            this.toastService.showToast('Error creating room: ' + err, 'error');
          }
        });
      }
    }
    this.router.navigate(['/admin/rooms']);
  }

  onClose(): void {
    this.router.navigate(['/admin/rooms']);
    this.cancel.emit();
  }

  onFacilityChange(facility: string, event: Event) {
    const facilities = this.roomForm.get('facility')?.value || [];
    const isChecked = (event.target as HTMLInputElement).checked;
  
    if (isChecked) {
      var upperCaseFacility = facility.toUpperCase();
      upperCaseFacility = upperCaseFacility.replace(' ', '_');
      facilities.push(upperCaseFacility);
    } else {
      const index = facilities.indexOf(facility);
      if (index > -1) {
        facilities.splice(index, 1);
      }
    }
    console.log(facilities);
    this.roomForm.get('facility')?.setValue(facilities);
  }

  onFileChange(event: any): void {
    this.selectedFile = event.target.files[0];
  }

  uploadFile(): void {
    if (this.selectedFile) {
      this.roomService.importRoom(this.selectedFile).subscribe(
        (event: HttpEvent<any>) => {
          switch (event.type) {
            case HttpEventType.UploadProgress:
              if (event.total) {
                const percent = Math.round((100 * event.loaded) / event.total);
                this.message = `Progress: ${percent}%`;
              }
              break;
            case HttpEventType.Response:
              this.message = `Success: ${event.body}`;
              this.showFileUpload = false;
              this.toastService.showToast('Room data imported successfully!', 'success');
              break;
          }
        },
        error => {
          if (error.status === 400) {
            this.message = `Error: Invalid file content - ${error.error}`;
          } else if (error.status === 500) {
            this.message = `Error: ${error.error}`;
          } else {
            this.message = `Error: ${error.message}`;
          }
          this.toastService.showToast('Error import room data: '+error.message, 'error');
        }
      );
    } else {
      this.message = 'Please select a file first';
    }
  }
}
