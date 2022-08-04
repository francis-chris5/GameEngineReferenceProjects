

from direct.showbase.ShowBase import ShowBase
from direct.actor.Actor import Actor
from panda3d.core import *



from GameObjects import *


class Game(ShowBase):
	def __init__(self):
		super().__init__()

		
		self.cam.setPos(15, -8, 4)
		self.cam.lookAt(0, 0, 0)

		self.light = AmbientLight('alight')
		self.light.setColor((0.8, 0.8, 0.8, 1))
		attachLight = render.attachNewNode(self.light)
		render.setLight(attachLight)

		self.land = self.loader.loadModel("land.bam")
		self.land.setScale(1, 1, 1)
		self.land.setPos(0, 0, -1)
		self.land.reparentTo(self.render)
		
		self.player = Character3D(
			base.mouseWatcherNode,
			Actor(
				"human_idle.bam", 
				{"Idle":"human_idle.bam", "Walk":"human_walk.bam"}
				)
			)
		self.player.getActor().reparentTo(self.render)
		
		self.taskMgr.add(self.player.chooseAnimation, "PlayerAnimation")
		self.taskMgr.add(self.player.move, "PlayerMove")
		

screen = Game()
screen.run()
