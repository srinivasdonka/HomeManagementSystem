import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'left-banner-image',
  templateUrl: './left-banner-image.component.html',
  styleUrls: ['./left-banner-image.component.scss']
})
export class LeftBannerImageComponent implements OnInit {
  imagePath: string;

  constructor() { }

  ngOnInit() {
    this.loadImage();
  }

  loadImage() {
    const imagesList = new Array("assets/images/slides/hm_2.jpg", "assets/images/slides/hm_3.jpg");
    const randomNum = Math.floor(Math.random() * imagesList.length);
    this.imagePath = imagesList[randomNum];
  }

}
