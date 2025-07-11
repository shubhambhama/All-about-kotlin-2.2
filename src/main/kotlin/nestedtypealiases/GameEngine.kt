package org.kotlin.nestedtypealiases

class GameEngine {
    typealias Position = Pair<Float, Float>
    typealias Velocity = Pair<Float, Float>
    typealias GameObjectId = String
    typealias ComponentData = Map<String, Any?>

    class Renderer {
        typealias Color = Triple<Int, Int, Int>
        typealias RenderLayer = Int
        typealias RenderCommand = (Position, Color) -> Unit
        typealias ShaderProgram = String

        private val renderQueue = mutableMapOf<RenderLayer, MutableList<RenderCommand>>()
        private val shaders = mutableMapOf<String, ShaderProgram>()

        fun addShader(name: String, program: ShaderProgram) {
            shaders[name] = program
        }

        fun queueRender(layer: RenderLayer, command: RenderCommand) {
            renderQueue.getOrPut(layer) { mutableListOf() }.add(command)
        }

        fun render() {
            renderQueue.keys.sorted().forEach { layer ->
                println("Rendering layer $layer")
                renderQueue[layer]?.forEach { command ->
                    command(Position(0f, 0f), Color(255, 255, 255))
                }
            }
            renderQueue.clear()
        }
    }

    class Physics {
        typealias Mass = Float
        typealias Force = Pair<Float, Float>
        typealias Collision = Triple<GameObjectId, GameObjectId, Position>
        typealias PhysicsBody = Map<String, Any?>

        private val bodies = mutableMapOf<GameObjectId, PhysicsBody>()
        private val collisions = mutableListOf<Collision>()

        fun addBody(id: GameObjectId, position: Position, mass: Mass) {
            bodies[id] = mapOf(
                "position" to position,
                "mass" to mass,
                "velocity" to Velocity(0f, 0f)
            )
        }

        fun applyForce(id: GameObjectId, force: Force) {
            val body = bodies[id]?.toMutableMap() ?: return
            val currentVelocity = body["velocity"] as? Velocity ?: Velocity(0f, 0f)
            val mass = body["mass"] as? Mass ?: 1f

            val acceleration = Pair(force.first / mass, force.second / mass)
            val newVelocity = Velocity(
                currentVelocity.first + acceleration.first,
                currentVelocity.second + acceleration.second
            )

            body["velocity"] = newVelocity
            bodies[id] = body
        }

        fun update(deltaTime: Float) {
            bodies.forEach { (id, body) ->
                val position = body["position"] as? Position ?: return@forEach
                val velocity = body["velocity"] as? Velocity ?: return@forEach

                val newPosition = Position(
                    position.first + velocity.first * deltaTime,
                    position.second + velocity.second * deltaTime
                )

                val updatedBody = body.toMutableMap()
                updatedBody["position"] = newPosition
                bodies[id] = updatedBody
            }
        }
    }

    private val renderer = Renderer()
    private val physics = Physics()
    private val gameObjects = mutableMapOf<GameObjectId, ComponentData>()

    fun createGameObject(id: GameObjectId, position: Position): GameObjectId {
        gameObjects[id] = mapOf(
            "position" to position,
            "active" to true,
            "created_at" to System.currentTimeMillis()
        )

        physics.addBody(id, position, 1f)
        return id
    }

    fun update(deltaTime: Float) {
        physics.update(deltaTime)
        renderer.render()
    }
}